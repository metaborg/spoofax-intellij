# JPS dependencies
The dependencies of the JPS plugin are listed in the `/src/main/resources/META-INF/plugin.xml` file under the `<compileServer.plugin>` tag. It is a semi-colon separated list of JAR names.

To update this list:

1. Ensure all dependencies of the project are also listed in the `pom.xml` file.
2. Ensure IntelliJ is registed in the local repo. Otherwise, run the `install-intellij` script.
3. Execute the following command:

	```
	mvn dependency:build-classpath -Dmdep.pathSeparator=":" -Dmdep.prefix='' -Dmdep.fileSeparator=":" -Dmdep.outputFile=classpath
	```
	
	This creates a file `classpath` with a `::` separated list of JAR dependencies.
	
4. Remove the following JARs from the list:

	* `slf4j-api`
	* `slf4j-simple`
	* `ant-1.5`

5. Replace all `::` by `;`.
6. Remove the `:` prefix.
7. Prefix the whole list by

	```
	org.metaborg.spoofax.intellij.jar;../classes/jpslib/slf4j-api-1.7.12.jar;../classes/jpslib/slf4j-simple-1.7.12;
	```
	
8. Copy the whole list, and replace the one under the `<compileServer.plugin classpath="">` element.
