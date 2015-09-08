# Passing data from IntelliJ IDEA to JPS
All kinds of information about the project, its modules, its dependencies and settings is configured in IntelliJ IDEA and needed in the JPS plugin. Since the IDE and the JPS plugin don't share a process, we need to somehow communicate the data over the process barrier. Serialization to the rescue.

## Persisting IDEA state
First we need to store anything we want to send from IntelliJ IDEA to JPS.

TODO

#### See also

* [Persisting State of Components](http://www.jetbrains.org/intellij/sdk/docs/basics/persisting_state_of_components.html)


## Model serializer extension
In the JPS plugin, extend the `JpsModelSerializerExtension` class to notify IntelliJ about the data serializers that you have implemented.

Register the serializer extension with the JPS plugin. To do this, create a file named `org.jetbrains.jps.model.serialization.JpsModelSerializerExtension` (no extension) in the `/src/main/resources/META-INF/services/` directory of the JPS module. In the file, write the fully qualified class name of the serializer extension. For example:

```
org.metaborg.spoofax.intellij.jps.JpsSpoofaxModelSerializerExtension
```

### Root model serializer
TODO

### Module options serializer
TODO

### Module dependency properties serializer
TODO

## Serializers
These are the serializers you can implement.

### Global extension serializer
Extend the `JpsGlobalExtensionSerializer` class. Implement the `loadExtension()` and `loadExtensionWithDefaultSettings()` methods. Usually you'd want to create a new JSP element and read its properties from the JDOM, then add it to the element container of the Global. See [Element containers](Element containers.md) for more info about using element containers.

> **Note**: The `configFileName` constructor parameter is relative to `StoragePathMacros.APP_CONFIG`. See [Persisting IDEA state](#Persisting IDEA state) for more information.

```
public class ContosoGlobalSerializer extends JpsGlobalExtensionSerializer {
    private static final String MY_NAME_NAME = "MY_NAME";

    public ContosoGlobalSerializer() {
        super("Contoso.xml", "ContosoGlobalConfig");
    }

    @Override
    public void loadExtensionWithDefaultSettings(JpsGlobal global) {
        loadExtensionFromJDom(global, null);
    }

    @Override
    public void loadExtension(JpsGlobal global, Element componentTag) {
        loadExtensionFromJDom(global, componentTag);
    }

    private void loadExtensionFromJDom(JpsGlobal global, @Nullable Element componentTag) {
        final ContosoGlobalConfigImpl configuration = new ContosoGlobalConfigImpl();

        if (componentTag != null) {
            final String myName = JDOMExternalizerUtil.readField(componentTag, MY_NAME_NAME);
            if (myName != null) {
                configuration.setMyName(myName);
            }
        }

        global.getContainer().setChild(ContosoGlobalConfigImpl.ROLE, config);
    }

    @Override
    public void saveExtension(JpsGlobal jpsGlobal, Element componentTag) {

    }
}
```

Return your serializer from the `JpsModelSerializerExtension.getGlobalExtensionSerializers()` method.

```
@NotNull @Override
public List<? extends JpsGlobalExtensionSerializer> getGlobalExtensionSerializers() {
	return Collections.singletonList(new ContosoGlobalSerializer());
}
```

#### See also

* [Use ServiceManager in ExternalBuilder](https://devnet.jetbrains.com/message/5502117#5502117)
* [`JpsIntelliLangModelSerializerExtension`](https://github.com/JetBrains/intellij-community/blob/master/plugins/IntelliLang/intellilang-jps-plugin/src/org/jetbrains/jps/intellilang/model/impl/JpsIntelliLangModelSerializerExtension.java)
* [`JpsIntelliLangConfigurationSerializer`](https://github.com/JetBrains/intellij-community/blob/master/plugins/IntelliLang/intellilang-jps-plugin/src/org/jetbrains/jps/intellilang/model/impl/JpsIntelliLangConfigurationSerializer.java)
* [`JpsIntelliLangConfiguration`](https://github.com/JetBrains/intellij-community/blob/master/plugins/IntelliLang/intellilang-jps-plugin/src/org/jetbrains/jps/intellilang/model/JpsIntelliLangConfiguration.java)
* [`JpsIntelliLangConfigurationImpl`](https://github.com/JetBrains/intellij-community/blob/master/plugins/IntelliLang/intellilang-jps-plugin/src/org/jetbrains/jps/intellilang/model/impl/JpsIntelliLangConfigurationImpl.java)
* [`JpsIntelliLangExtensionService`](https://github.com/JetBrains/intellij-community/blob/master/plugins/IntelliLang/intellilang-jps-plugin/src/org/jetbrains/jps/intellilang/model/JpsIntelliLangExtensionService.java)
* [`JpsIntelliLangExtensionServiceImpl`](https://github.com/JetBrains/intellij-community/blob/master/plugins/IntelliLang/intellilang-jps-plugin/src/org/jetbrains/jps/intellilang/model/impl/JpsIntelliLangExtensionServiceImpl.java)
* [`Configuration`](https://github.com/JetBrains/intellij-community/blob/master/plugins/IntelliLang/src/org/intellij/plugins/intelliLang/Configuration.java)

### Project extension serializer
TODO

### Module properties serializer
TODO

### Module source root properties serializer
TODO

### Library root type serializer
TODO

### Library properties serializer
TODO

### SDK root type serializer
TODO

### SDK properties serializer
TODO

### Facet configuration serializer
TODO

### Packaging element serializer
TODO

### Artifact type properties serializer
TODO

### Artifact extension serializer
TODO

### Classpath serializer
TODO

### Run configuration properties serializer
TODO



