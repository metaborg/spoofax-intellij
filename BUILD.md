# Building from source

## Getting the source

First of all, clone the project:

	git clone https://github.com/metaborg/spoofax-intellij.git


## Setting up IntelliJ IDEA

> **Note**: You can invoke the `scripts/install-intellij` script to
> perform the following steps automatically.

1. Install IntelliJ IDEA Community Edition.
2. Clone the IntelliJ IDEA Community Edition source code from `https://github.com/JetBrains/intellij-community`.
3. In IntelliJ IDEA, add the Java SDK.
4. Also add the IntelliJ IDEA Plugin SDK, and set its sourcepath to the downloaded IDEA source.


## Setting up the project

1. Open the project (_File_ &rarr; _Open project_; or <kbd>Ctrl</kbd> + <kbd>O</kbd>).
2. Open the module's Project Structure dialog (_File_ &rarr; _Project Structure_; or <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Shift</kbd> + <kbd>S</kbd>)
3. Set the _Module SDK_ to the IntelliJ IDEA Plugin SDK.


## Running and debugging

To run/debug the plugin, use the `Spoofax Plugin` run/debug configuration.

To debug the target builder, run the `Spoofax Plugin (Builder)` run configuration. In the newly started IntelliJ IDEA Plugin instance, click _Build_ &rarr; _Make Project_. The plugin instance will wait for you to attach a debugger. Now use the `Spoofax Remote` debug configuration to attach a debugger to the plugin instance. The build will continue with debugging enabled.


## Deploying

To deploy the plugin, click _Build_ &rarr; _Prepare All Plugin Module For Deployment_.
