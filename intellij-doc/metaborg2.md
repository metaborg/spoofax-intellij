# Elements and Element Types

## Element types
Element types. `P` is the element with the type's properties. 

### Implementation
In addition to implementing the element type interface (some extension of `JpsElementType<P>`), element type implementations must extend the `JpsElementTypeBase<P>` class. When `P` is `JpsDummyElement`, then extend `JpsElementTypeWithDummyProperties` instead.


#### Module type
A trivial module type without any specific properties:

```
public class ContosoJpsModuleType
  extends JpsElementTypeWithDummyProperties
  implements JpsModuleType<JpsDummyElement> {
  
  public static final ContosoJpsModuleType INSTANCE = new ContosoJpsModuleType();
  
  private ContosoJpsModuleType() {}

}
```

A simple module type with a single POJO class:

```
public class ContosoJpsModuleType
  extends JpsElementTypeBase<JpsSimpleElement<ContosoModuleProperties>>
  implements JpsModuleType<JpsSimpleElement<ContosoModuleProperties>> {
  
  public static final ContosoJpsModuleType INSTANCE = new ContosoJpsModuleType();
  
  private ContosoJpsModuleType() {}

}
```



## Element


### Creation
In general, use the Singleton `JpsElementFactory.getInstance()` instance to create new elements.

#### Global
You can't create the _Global_. Use `JpsModel.getGlobal()` to get the Global.

#### Global reference
```
JpsElementReference<JpsGlobal> element =
    JpsElementFactory.getInstance()
    .createGlobalReference();
```

#### Project
You can't create the _Project_. Use `JpsModel.getProject()` to get the Project.

#### Project reference
```
JpsElementReference<JpsProject> element =
    JpsElementFactory.getInstance()
    .createProjectReference();
```

#### Module

* `name : String` — The name of the module.
* `type : JpsModuleType<P>` — The module type.
* `properties : P` — The module properties.

```
JpsModule elements =
    JpsElementFactory.getInstance()
    .createModule(name, type, properties);
```

#### Module reference

* `name : String` — The name of the module.

```
JpsModuleReference element =
    JpsElementFactory.getInstance()
    .createModuleReference(name);
```

#### Module source root

* `url : String` — The URL of the module source root.
* `type : JpsModuleSourceRootType<P>` — The the module source root type.
* `properties : P` — The module source root properties.

```
JpsModuleSourceRoot element =
    JpsElementFactory.getInstance()
    .createModuleSourceRoot(url, type, properties);
```

#### Library

* `name : String` — The name of the library.
* `type : JpsLibraryType<P>` — The library type.
* `properties : P` — The library properties.

```
JpsTypedLibrary<P> element =
    JpsElementFactory.getInstance()
    .createLibrary(name, type, properties)
```

#### Library reference

* `name : String` — The name of the library.
* `reference : JpsElementReference<? extends JpsCompositeElement>` — Reference to the parent of the library.

```
JpsLibraryReference element =
    JpsElementFactory.getInstance()
    .createLibraryReference(name, parent);
```

#### SDK

* `name : String` — The name of the SDK.
* `homePath : String` — The home path of the SDK.
* `versionString : String` — The version string of the SDK.
* `type : JpsLibraryType<P>` — The SDK type.
* `properties : P` — The SDK properties.

```
JpsLibrary library =
    JpsElementFactory.getInstance()
    .createSdk(name, homePath, versionString, type, properties);
```

#### SDK reference

* `name : String` — The name of the SDK.
* `type : JpsLibraryType<P>` — The SDK type.

```
JpsSdkReference<P> =
    JpsElementFactory.getInstance()
    .createSdkReference(name, sdkType);
```

#### Simple element

* `data : D` — The data.

```
JpsSimpleElement<ContosoModuleProperties> element =
    JpsElementFactory.getInstance()
    .createSimpleElement(properties);
```

#### Dummy element
```
JpsDummyElement element =
    JpsElementFactory.getInstance()
    .createDummyElement();
```


### Implementation
Any JPS element should implement the `JpsElement` interface, but do this by extending the `JpsElementBase<Self>` class instead.

