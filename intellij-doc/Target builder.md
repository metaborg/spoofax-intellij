# Target builder

Implementing a target builder.

## Cancelling
Priodically check whether the build was cancelled.

```
// Returns whether the build was cancelled.
context.getCancelStatus().isCanceled()

// Throws a StopBuildException when the build was cancelled.
context.checkCanceled();
```


## Progress messages
To display a progress message:

```
context.processMessage(new ProgressMessage("Compiling..."));
```


## Build messages
To add a build message:

```
context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.WARNING, "Compilation not implemented!"));
```

