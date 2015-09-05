# Logger

This error

> loader constraint violation: when resolving method "`org.slf4j.impl.StaticLoggerBinder.getLoggerFactory()Lorg/slf4j/ILoggerFactory;`" the class loader (instance of `com/intellij/ide/plugins/cl/PluginClassLoader`) of the current class, `org/slf4j/LoggerFactory`, and the class loader (instance of `com/intellij/util/lang/UrlClassLoader`) for the method's defining class, `org/slf4j/impl/StaticLoggerBinder`, have different Class objects for the type `org/slf4j/ILoggerFactory` used in the signature.

is caused by trying to load two different instances of the `slf4j-api.jar`. The solution is to ensure `slf4j-api` is a (Maven scope) _provided_ dependency, and not copied to the plugin's `lib` directory.

You can check that `slf4j-api.jar` should not be in this directory:

	~/.IdeaIC14/system/plugins-sandbox/plugins/org.metaborg.spoofax.intellij.idea/lib

In IntelliJ's _Project Settings_, you can remove the `slf4j-api.jar` from the classpath of libraries that have it in them. However, IntelliJ shouldn't even add them in the first place when the library has `slf4j-api` in scope _provided_.
