# Passing data from IntelliJ IDEA to JPS
All kinds of information about the project, its modules, its dependencies and settings is configured in IntelliJ IDEA and needed in the JPS plugin. Since the IDE and the JPS plugin don't share a process, we need to somehow communicate the data over the process barrier. Serialization to the rescue.

## Model serializer extension
Extend the `JpsModelSerializerExtension` class to notify IntelliJ about the data serializers that you have implemented.

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
TODO

### Project extension serializer
TODO

### Module properties serializer
Extend the `JpsModulePropertiesSerializer<P>` class, and implement the `loadProperties` and `saveProperties` methods.

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



