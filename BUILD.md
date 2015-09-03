# Building from source

## Getting the source

First of all, clone the project:

	git clone https://github.com/metaborg/spoofax-intellij.git


## Setting up IntelliJ IDEA

Run the `scripts/install-intellij` script. This script downloads
and installs IntelliJ IDEA Community Edition, downloads its sources,
and installs the IDEA SDK in a local repository for use by the plugin.

## Setting up the project

1. Open the project (_File_ &rarr; _Open project_; or <kbd>Ctrl</kbd> + <kbd>O</kbd>).
2. Open the module's Project Structure dialog (_File_ &rarr; _Project Structure_; or <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>Shift</kbd> + <kbd>S</kbd>)
3. Set the _Module SDK_ to the IntelliJ IDEA Plugin SDK.


## Running and debugging

To run/debug the plugin, use the `Spoofax IDEA Plugin` run/debug configuration.

To debug the target builder, run the `Spoofax IDEA Plugin (Debug JPS)` run configuration. In the newly started IntelliJ IDEA Plugin instance, click _Build_ &rarr; _Make Project_. The plugin instance will wait for you to attach a debugger. Now use the `Spoofax JPS Plugin` debug configuration to attach a debugger to the plugin instance. The build will continue with debugging enabled.


## Deploying

To deploy the plugin, click _Build_ &rarr; _Prepare All Plugin Modules For Deployment_.
