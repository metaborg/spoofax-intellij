# Passing data from IntelliJ IDEA to JPS
All kinds of information about the project, its modules, its dependencies and settings is configured in IntelliJ IDEA and needed in the JPS plugin. Since the IDE and the JPS plugin don't share a process, we need to somehow communicate the data over the process barrier. Serialization to the rescue.

## Serializing and deserializing IntelliJ IDEA state
First we need to store anything we want to keep between IntelliJ sessions. This also happens to be readable by the JPS plugin.

### State class
Create a class that will contain the state you want to save. It must be a simple class, preferably `final`, with `equals` and `hashCode` overridden, and defaults assigned in a parameterless constructor. For example:


```
public final class MyGlobalState {

    private String myName;
    public String getMyName() { return this.myName; }
    public void setMyName(String value) { this.myName = value; }

    public MyGlobalState() {
        // Defaults
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyGlobalState))
            return false;
        return equals((MyGlobalState)obj);
    }

    public boolean equals(MyGlobalState other) {
        if (other == this)
            return true;
        if (other == null)
            return false;

        return new EqualsBuilder()
                .append(this.myName, other.myName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 31)
                .append(this.myName)
                .toHashCode();
    }

}
```

### PersistentStateComponent class
In IntelliJ the `PersistentStateComponent<T>` interface is used for classes that can store their own state. However, these classes must be either an:

* application component;
* project component;
* module component; or
* application service;
* project service;
* module service.

In this example we'll create an _application service_, that is, a class for which only one instance exists for the whole IntelliJ application.

The class must implement the `PersistentStateComponent<T>` interface, where `T` is the state class. It must also have a parameterless constructor that initializes the default state, override the `equals` and `hashCode` methods, and have the `@State` attribute applied to it. The `@State` attribute describes the object and where it must be stored. See [Persisting State of Components](<http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html>) for more information.

```
@State(
        name = "MyGlobalService",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.APP_CONFIG + "/My.xml")
        }
)
public final class MyGlobalService implements PersistentStateComponent<MyGlobalState> {

    private MyGlobalState state;

    public MyGlobalService() {
        state = new SpoofaxGlobalState();
    }

    @Nullable @Override public MyGlobalState getState() { return this.state; }

    @Override
    public void loadState(MyGlobalState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MyGlobalService))
            return false;
        return this.state.equals(((MyGlobalService)obj).state);
    }

    @Override
    public int hashCode() {
        return this.state.hashCode();
    }
}
```

Register your application service in the `plugin.xml` file:

```
<extensions defaultExtensionNs="com.intellij">
  <applicationService serviceImplementation="org.contoso.MyGlobalService"/>
</extensions>
```

Now the state is persisted between IntelliJ sessions.

### JPS element
We want to load the state in JPS, and this means we have to add it to the JPS model. Since the JPS model consists of elements, we have to define our own. For example:

```
public class MyGlobalConfig extends JpsElementBase<MyGlobalConfig> {
    public static final JpsElementChildRole<MyGlobalConfig> ROLE = JpsElementChildRoleBase.create("My");

    private MyGlobalState state = new MyGlobalState();
    public MyGlobalState getState() { return this.state; }
    public void loadState(MyGlobalState value) { this.state = value; }

    @NotNull
    @Override
    public MyGlobalConfig createCopy() {
        return new MyGlobalConfig();
    }

    @Override
    public void applyChanges(@NotNull MyGlobalConfig modified) {
        this.state = modified.state;
    }
}
```

### Deserializing the JPS element
And finally we have to deserialize the JPS element and add it to the model. See [Element containers](Element containers.md) for more information about adding elements to element containers. See [Serialization](Serialization.md) for more information about serialization.


```
public class MyGlobalSerializer extends JpsGlobalExtensionSerializer {

    public MyGlobalSerializer() {
        super("My.xml", "MyGlobalService");
    }

    @Override
    public void loadExtensionWithDefaultSettings(JpsGlobal global) {
        loadExtensionWithState(global, null);
    }

    @Override
    public void loadExtension(JpsGlobal global, Element element) {
        MyGlobalState state = XmlSerializer.deserialize(element, MyGlobalState.class);
        loadExtensionWithState(global, state);
    }

    private void loadExtensionWithState(JpsGlobal global, MyGlobalState state)
    {
        final MyGlobalConfig config = new MyGlobalConfig();
        if (state != null)
            config.loadState(state);
        global.getContainer().setChild(MyGlobalConfig.ROLE, config);
    }

    @Override
    public void saveExtension(JpsGlobal jpsGlobal, Element element) {
        throw new UnsupportedOperationException("The `saveExtension()` method is not supported.");
    }
}
```

> **Note**: `saveExtension` is not currently used.

And return it from the JPS model serializer extension

```

public class MyModelSerializerExtension extends JpsModelSerializerExtension {

    @NotNull
    @Override
    public List<? extends JpsGlobalExtensionSerializer> getGlobalExtensionSerializers() {
        return Collections.singletonList(new SpoofaxGlobalSerializer());
    }
}
```

which we register by creating a file `org.jetbrains.jps.model.serialization.JpsModelSerializerExtension` (no extension) in the `/src/main/resources/META-INF/services` folder of the JPS module, with the fully qualified name of the serializer extension in it. For example:

```
org.contoso.jps.MyModelSerializerExtension
```
  

### See also

* [Persisting State of Components](<http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html>)
* [IntelliJ IDEA Plugin Structure](<https://confluence.jetbrains.com/display/IDEADEV/IntelliJ+IDEA+Plugin+Structure#IntelliJIDEAPluginStructure-PluginComponents>)



