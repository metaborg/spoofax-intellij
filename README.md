[![Jenkins](https://img.shields.io/jenkins/s/http/buildfarm.metaborg.org/spoofax-intellij.svg)](http://buildfarm.metaborg.org/job/spoofax-intellij/)
[![GitHub license](https://img.shields.io/github/license/metaborg/spoofax-intellij.svg)](./LICENSE.md)

# Spoofax plugin for IntelliJ IDEA
Develop and use languages with the [Spoofax Language Workbench](http://www.spoofax.org/) in IntelliJ IDEA.

Want to learn more? [See the wiki](https://github.com/metaborg/spoofax-intellij/wiki).

Want to contribute to this plugin? [More information](./CONTRIBUTE.md).


## Quick Start
Install the plugin.

Create a new Spoofax language specification by going to the _File_
→ _New_ → _Project_ menu, and selecting _Spoofax Language_.
Finish the wizard. Go to [the Spoofax website](http://www.spoofax.org/)
for more information.

![New Spoofax Project](./intellij-doc/NewProjectWizard.png)


## Features
The Spoofax for IntelliJ IDEA plugin has the following features:

* Create new Spoofax Language Specification project.
* Import existing Spoofax Language Specification project (e.g. Eclipse project).
* Syntax highlighting. (*)
* Reference resolution. (*)
* Load/unload languages on-the-fly.
* Use your own language in the same editor.


## Limitations
The Spoofax for IntelliJ IDEA plugin has the following limitations:

* Languages are loaded application-wide.
