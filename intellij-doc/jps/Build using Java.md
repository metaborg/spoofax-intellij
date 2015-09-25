# Build using Java
You can have your JPS build use Java, but you have to fulfill the following requirements:

* In the `ModuleBuilder` (that creates a new module), add some source roots to the content entry of the root model.

      ContentEntry contentEntry = doAddContentEntry(rootModel);
      contentEntry.addSourceFolder("src", false);
      
* Implement the `ModuleLevelBuilder` interface (that builds a module) for compile units that you want to execute directly before or directly after the Java build.
* Implement the `TargetBuilder` interface for compile units that you want to execute before or after the Java build. (Also implement the corresponding build target and build target type.)
* Add Java as a dependency to one of the build target's `computeDependencies()` methods.

      dependencies.add(new ModuleBuildTarget(super.myModule, JavaModuleBuildTargetType.PRODUCTION));
      
* Using the `BuildTarget`'s `isCompiledBeforeModuleLevelBuilders()` method, you can control whether a dependency is executed before or after the Java builder.


