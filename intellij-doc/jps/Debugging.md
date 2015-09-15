# Debugging

## Debugging External Build
You want to debug the external build, but since it's run in a separate process, you can't simple debug the IntelliJ IDE.

### Setup
1. Add a new _Plugin_ Run/Debug configuration, or duplicate an existing one.
2. To the configuration's _VM Options_, add the following option:

    ```
    -Dcompiler.process.debug.port=5005
    ```

3. Add a new _Remote_ Run/Debug configuration. By default it will be listening to port 5005.

### Use
1. Run (not Debug) the IntelliJ plugin using the modified _Plugin_ Run/Debug configuration.
2. In the new IntelliJ instance, invoke _Make_. IntelliJ will wait for you to attach a remote debugger.
3. Debug using the the _Remote_ Run/Debug configuration. The external build process continues, and you can debug it.


## VM options
You can set VM options for the external build process in _Settings_ &rarr; _Build, Execution, Deployment_ &rarr; _Compiler_, in the _Additional build process VM options_ field.


## External build log
The log of the external build can be found in the `%idea.path%/system/log/build-log/build.log` file.

> **Note**: The `%idea.path%` is usually `~/.IdeaIC14/`, but for the plugin instance of IntelliJ it's `~/.IdeaIC14/system/plugins-sandbox/`.

You can modify the log verbosity and other log options in the `%idea.path%/system/log/build-log/build-log.properties` file.


## External build VM log
The VM might log its own data (e.g. when you specified `-verbose:class` as a VM argument). You can find this data in the `%idea.path%/system/log/idea.log` file of IntelliJ.
