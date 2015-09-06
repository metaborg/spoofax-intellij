# Plugin dependencies
The plugin may depend on other plugins. To make this work:

1. Add the jars of the plugin dependency to the _IDEA SDK classpath_.
2. Add a `<depends>` tag to the `META-INF/plugin.xml` file. For example:

       <depends>org.jetbrains.idea.maven</depends>

> **Note**: The ID of the plugin dependency can be found in the plugin's `META-INF/plugin.xml` file's `<id>` tag.


## Maven plugin
To depend on the Maven plugin:

1. Copy the `maven.jar` from the IntelliJ IDEA CE installation's `plugins/maven/lib` directory.
2. Paste it somewhere you can find it.
3. In the plugin's _Project Structure_ dialog, go to _SDKs_.
4. Select the _IntelliJ IDEA SDK_, then the _Classpath_ tab.
5. Add the `maven.jar` to the SDK's classpath.
6. Add to `META-INF/plugins.xml` the line: `<depends>org.jetbrains.idea.maven</depends>`.


## Spoofax plugin
To depend on the Spoofax plugin:

1. Copy the `org.metaborg.spoofax.intellij.idea.jar` from {somewhere}.
2. Paste it somewhere you can find it.
3. In the plugin's _Project Structure_ dialog, go to _SDKs_.
4. Select the _IntelliJ IDEA SDK_, then the _Classpath_ tab.
5. Add the `org.metaborg.spoofax.intellij.idea.jar` to the SDK's classpath.
6. Add to `META-INF/plugins.xml` the line: `<depends>org.metaborg.spoofax.intellij.idea</depends>`.

> **Fixme**: Where can we find the JAR? Do we have to copy all dependencies with it? How can we use this within the same project where the plugins are different modules?

> **Note**: this seems to work only when all plugin dependencies are added to the SDK classpath. You can get all JARs from the `lib` directory in the ZIP generated when you click _Build_ &rarr; _Prepare Plugin Module 'org.metaborg.spoofax.intellij.idea' For Deployment_. The Meta project should not have any dependencies that the IDEA plugin has?
