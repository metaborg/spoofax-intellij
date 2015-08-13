# External builder

If you can't use IntelliJ IDEA's built-in _Make_ functionality, you can install your own. IntelliJ IDEA uses _JetBrains Project System_ (JPS) to build projects. JPS has its own model, represented by classes such as `JpsProject` and `JpsModule`.


## Implementation

### Project setup
The JPS functionality should be implemented in a separate module named `<pluginname>-jps-plugin` in the `jps-plugin` subdirectory of the IDEA plugin project. You can just create a new module for this, but you should select the IntellIJ IDEA SDK for the module.

In the `src` directory of the JPS plugin module, create the directory `META_INF/services`. This directory will hold the special files that specify where JPS can find all the services.

Additionally, you might want to create another module with functionality common to both the IDEA plugin and the JPS plugin, for example, `<pluginname>-jps-common` in the `jps-common` subdirectory of the IDEA plugin project.

Create the correct dependencies between the modules: the IDEA plugin depends on both the JPS plugin and the common JPS module, and the JPS plugin only depends on the common JPS module.

Register the JPS plugin in the `META-INF/plugin.xml` of the IDEA plugin:

    <extensions defaultExtensionNs="com.intellij">
      <compileServer.plugin classpath="example-jps-plugin.jar;example-jps-common.jar"/>
    </extensions>
    
> **Note**: For some reason IntelliJ complains about the `classpath` attribute. You can ignore that.


### Build root descriptor
A _build root descriptor_ describes a source root of a build target. Its simplest implementation just stores the root `File` and the `BuildTarget`, and exposes these through the `getRootFile` and `getTarget` getters.

For example, see the [`SpoofaxSourceRootDescriptor`](#) class.


### Build target
A _build target_ is a distinct unit of compilation work. Build targets may depend on one another. There are two kinds of build targets:

1. _Total build target_, a.k.a. `BuildTarget<R>`. The build target (and its corresponding `TargetBuilder` are expected to handle the compilation from start to finish (i.e. vertically). 
2. _Phase build target_, a.k.a. `ModuleBasedTarget<R>`. The build target handles a single phase in the compilation process across multiple modules (i.e. horizontally).

The _phase build target_ is useful to add preprocessing and code generation phases to the existing compilation pipeline (e.g. the Java pipeline). You cannot influence the order in which build targets are executed in the same phase.

The _total build target_ is useful to change the compilation pipeline from start to finish, for example, when you have you own compiler.

You need to implement your own build target, which extends from either `BuildTarget<R>` or `ModuleBasedTarget<R>`. The [`SpoofaxBuildTarget`](#) class is an example of a total build target.


### Build target type
The _build target type_ describes a class of build targets. You have to implement your own, extending from either `BuildTargetType<T>` or `ModuleBasedBuildTargetType<T>`, depending on the kind of build target. The ID of the build target type must be constant. See also the [`SpoofaxBuildTargetType`](#) class.


### Target builder
The _target builder_ does the actual compilation work. Extend your own from the `TargetBuilder<R, T>` abstract class, and implement the `build` method.


### Builder service
You have to register your builders and target types with JPS through a _builder service_. Extend your own from `BuilderService`, see [`SpoofaxBuilderService`](#) for an example. In the `META-INF/services` directory, create a new text file named `org.jetbrains.jps.incremental.BuilderService` (no extension) with as its first and only line the fully qualified identifier of the builder service.


### Scope provider
You need a _build target scope provider_ to inform IntellIJ IDEA what scope your build target will handle. Some build targets handle only files of a particular file type or in a specific directory. Other targets handle the whole project. Create your scopes using the methods of the `CmdlineProtoUtil` class. See [`SpoofaxBuildTargetScopeProvider`](#) for an example.

Register your scope provider with the IDEA plugin in the `META-INF/plugin.xml` file:

    <extensions defaultExtensionNs="com.intellij">
      <compiler.buildTargetScopeProvider implementation="org.example.ExampleBuildTargetScopeProvider"/>
    </extensions>


## Disabling the Java builder
The Java builder is enabled by default. You can disable it with this method in your target builder:

    @Override
    public void buildStarted(CompileContext context) {
        JavaBuilder.IS_ENABLED.set(context, Boolean.FALSE);
    }



## Reporting errors and warnings
In the target builder, you can report errors, warnings, and all other sorts of messages through the `context.processMessage()` method. For example, to show an error:

	context.processMessage(new CompilerMessage("Example", BuildMessage.Kind.ERROR, "Here is a random error!"));



> **Note**: Your target builder should not be applied to modules it cannot compile. Or otherwise you shouldn't disable the Java builder. ([See also](https://github.com/JetBrains/intellij-scala/blob/89f4c6420f060ed6c64fab9ba9ddec3b43e744a7/jps-plugin/src/org/jetbrains/jps/incremental/scala/SbtBuilder.scala#L31).)


## See also

* [External Builder API and Plugins](http://www.jetbrains.org/intellij/sdk/docs/reference_guide/frameworks_and_external_apis/external_builder_api.html)
* [Activate DummySourceGeneratingCompiler in test plugin for test purposes](https://devnet.jetbrains.com/message/5484095)
