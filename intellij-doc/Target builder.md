# Target builder
The new build system uses the notion of build targets. A build target is a discrete compilation unit, and build targets can have dependencies on one another. The build engine uses the build targets to determine the order in which targets are processed. There are two kinds of build targets: ` 

## Kinds of builders
The compiler must be implemented by extending either `ModuleLevelBuilder` or `TargetBuilder<R, T>`.

### Module level builder
A module level builder is part of the Java compilation pipeline. You can use it to extend the pipeline, for example, to transform files before compilation, or to check code after compilation.

### Target builder
A target builder is for compilation of modules that have nothing to do with Java.


## Implementation

Implementing a target builder.

### Cancelling
Priodically check whether the build was cancelled.

```
// Returns whether the build was cancelled.
context.getCancelStatus().isCanceled()

// Throws a StopBuildException when the build was cancelled.
context.checkCanceled();
```


### Progress messages
To display a progress message:

```
context.processMessage(new ProgressMessage("Compiling..."));
```


### Build messages
To add a build message:

```
context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.WARNING, "Compilation not implemented!"));
```

