[![Jenkins](https://img.shields.io/jenkins/s/https/buildfarm.metaborg.org/job/metaborg/job/spoofax-releng/job/master.svg)](https://buildfarm.metaborg.org/job/metaborg/job/spoofax-releng/job/master/)
[![GitHub license](https://img.shields.io/github/license/metaborg/spoofax-intellij.svg)](./LICENSE.md)

# Spoofax Language Workbench plugin for IntelliJ IDEA
Develop and use languages with the [Spoofax Language Workbench][1] in IntelliJ IDEA.

Want to learn more? [Read the manual][2].

## Quick Start
To install the plugin, either:

* clone this repository, then execute `./gradlew runIde` (or `gradlew.bat runIde` on Windows) from the repository's root to start an instance of IntelliJ IDEA with the Spoofax plugin loaded; or
* ensure you have Git and a JDK installed, then execute this from the command line; or

  ```
  curl https://raw.githubusercontent.com/metaborg/spoofax-intellij/master/repository/install.sh -sSLf | bash
  ```

---

To create a new Spoofax language specification, go to the _File_ → _New_ → _Project_ menu, and select _Spoofax Language_. Finish the wizard.

![New Spoofax Project](https://spoofax.readthedocs.org/en/latest/_images/newprojectform_langspec_selectmetaborgsdk.png)

Or to create a Java project in which you can use Spoofax languages, go to the _File_ → _New_ → _Project_ menu, and select _Java_. Check the _Metaborg_ library and finish the wizard.

![New Java Project](https://spoofax.readthedocs.org/en/latest/_images/newprojectform_checkmetaborgframework.png)

Want to learn more? [Read the manual][2].



## Features
The Spoofax for IntelliJ IDEA plugin has the following features:

- Create new Spoofax Language Specification project.
- Import existing Spoofax Language Specification project (e.g. Eclipse/Maven/Gradle projects).
- Syntax highlighting.
- Reference resolution for most languages.
- Load/unload languages on-the-fly.
- Use your own language in the same editor.



## Limitations
The Spoofax for IntelliJ IDEA plugin has the following limitations:

* Languages are loaded application-wide.



## License
Copyright 2016-2018 Daniel Pelsmaeker

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an **"as is" basis, without warranties or conditions of any kind**, either express or implied. See the License for the specific language governing permissions and limitations under the License.


[1]: http://www.spoofax.org/
[2]: http://www.metaborg.org/en/latest/source/langdev/manual/env/intellij/index.html
