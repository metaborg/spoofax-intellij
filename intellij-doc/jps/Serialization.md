# Serialization

You can implement your own serializers to deserialize data coming from the IntelliJ IDEA. See [Passing data from IntelliJ IDEA to JPS](Passing data from IntelliJ IDEA to JPS.md) for more information.

## Model serializer extension
In the JPS plugin, extend the `JpsModelSerializerExtension` class to notify IntelliJ about the data serializers that you have implemented.

Register the serializer extension with the JPS plugin. To do this, create a file named `org.jetbrains.jps.model.serialization.JpsModelSerializerExtension` (no extension) in the `/src/main/resources/META-INF/services/` directory of the JPS module. In the file, write the fully qualified class name of the serializer extension. For example:

```
org.metaborg.spoofax.intellij.jps.JpsSpoofaxModelSerializerExtension
```

## Serializers
These are the serializers you can implement.

### Root model serializer
TODO

### Module options serializer
TODO

### Module dependency properties serializer
TODO


### Global extension serializer
To implement a global extension serializer:

* Extend the `JpsGlobalExtensionSerializer` class, and implement its methods.
* Return your serializer from the `JpsModelSerializerExtension.getGlobalExtensionSerializers()` method.

	For example:
	
	```
	@NotNull @Override
	public List<? extends JpsGlobalExtensionSerializer> getGlobalExtensionSerializers() {
		return Collections.singletonList(new ContosoGlobalSerializer());
	}
	```

> **Note**: The `configFileName` constructor parameter is relative to `StoragePathMacros.APP_CONFIG`.


#### See also

* [Use ServiceManager in ExternalBuilder](https://devnet.jetbrains.com/message/5502117#5502117)


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



