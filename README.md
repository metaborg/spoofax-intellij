[![Jenkins](https://img.shields.io/jenkins/s/http/buildfarm.metaborg.org/spoofax-intellij.svg)](http://buildfarm.metaborg.org/job/spoofax-intellij/)
[![GitHub license](https://img.shields.io/github/license/metaborg/spoofax-intellij.svg)](./LICENSE.md)

# Spoofax plugin for IntelliJ IDEA
Develop and use languages with the [Spoofax Language Workbench][1]
in IntelliJ IDEA.

Want to learn more? [Read the manual][2].

Want to contribute to this plugin? [More information][3].


## Quick Start
Install the plugin.

To create a new Spoofax language specification, go to the _File_
→ _New_ → _Project_ menu, and select _Spoofax Language_. Finish the wizard.

![New Spoofax Project](https://spoofax.readthedocs.org/en/latest/_images/newprojectform_langspec_selectmetaborgsdk.png)

Or to create a Java project in which you can use Spoofax languages, go to the
_File_ → _New_ → _Project_ menu, and select _Java_. Check the _Metaborg_
library and finish the wizard.

![New Java Project](https://spoofax.readthedocs.org/en/latest/_images/newprojectform_checkmetaborgframework.png)

Want to learn more? [Read the manual][2].


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




[1]: http://www.spoofax.org/
[2]: https://spoofax.readthedocs.org/en/latest/source/langdev/manual/env/intellij/index.html
[3]: ./CONTRIBUTE.md
