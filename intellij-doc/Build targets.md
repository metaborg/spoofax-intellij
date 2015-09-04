# Build targets
A _build target_ represents a single unit of the compilation process. When a given project's external build (e.g. Make) is invoked, the following happens:

In the IDE process:

1. Pre-compile tasks are executed.
2. Some source generation tasks that depend on the PSI are executed.
3. Any `BuildTargetScopeProvider` implementations are called to determine the set of build targets to compile.

Then the external build process is spawned, where:

1. The project model is loaded, represented by a `JpsModel` instance.
2. The full tree of build targets is determined, based on the dependencies of each build target to be compiled.
3. For each target, the set of builders capable of building this target is calculated.
4. For every target and every builder, the `build()` method is called.

	> **Note**: For module-level builders the order of invoking builders for a single target is determined by their category. For other builders the order is undefined.

5. Caches to record the state of the compilation are saved.

Then back in the IDE process:

1. Compilation messages are displayed in the IDE UI.
2. Post-compile tasks are executed.




## `BuildTargetScopeProvider`
The build target scope provider is used to determine the set of build targets to compile. You have to implement the `getBuildTargetScopes` method to return a list of `TargetTypeBuildScope` objects. You usually return just one `TargetTypeBuildScope` object.

> **Note**: The `forceBuild` boolean is `true` when rebuilding, and `false` otherwise.

For each module you want to compile, you have to add its target ID to the build scope, and specify the build target type. For example, with the list of target IDs in `targetIds`

```
return Collections.singletonList(
	CmdlineProtoUtil.createTargetsScope(
		MyBuildTargetType.INSTANCE.getTypeId(),
		targetIds,
		forceBuild
	)
);
```

> **Note**: If you've implemented your own `ModuleType`, you'd use the module's `ModuleType` (see `ModuleType<?>.get(Module)`) to determine whether it's the type of module you can build. You can get a list of affected modules using `baseScope.getAffectedModules()`.

> **Note**: To derive the target ID from a module, you can simply do `module.getName()`.

Or, if you want to compile all targets, you can simply

```
return Collections.singletonList(
	CmdlineProtoUtil.createAllTargetsScope(
		MyBuildTargetType.INSTANCE.getTypeId(),
		forceBuild
	)
);
```

> **See also**: The `CompileScopeUtil` class.

You register your build target scope provider in the `plugin.xml` file.

```
<compiler.buildTargetScopeProvider implementation="org.example.MyBuildTargetScopeProvider"/>
```




## See also

* [IntelliJ Platform SDK DevGuide - External Builder API and Plugins](http://www.jetbrains.org/intellij/sdk/docs/reference_guide/frameworks_and_external_apis/external_builder_api.html)
