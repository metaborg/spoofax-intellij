# IntelliJ Plugin development with Maven

IntelliJ IDEA has excellent support for Maven, _except_ when you're trying to develop an IntelliJ plugin. IntelliJ has its own project type for plugin projects, and this can't be used when the project is a Maven project. The trick to using Maven for plugin development is to let IntelliJ ignore the `pom.xml`, and force the IDE a bit to execute the Maven goals nonetheless.

## New plugin project
First we create a new IntelliJ Plugin module, then fix it up to match Maven's default directory layout.

1. Create a new _IntelliJ Platform Plugin_ module. Don't forget to select the IntelliJ IDEA SDK as the module's SDK. 

    > This creates a new module with a `/META-INF/plugin.xml` file, a `/src` directory, and finally the module metadata in the `/my-plugin.iml` file and `/.idea` directory.

2. In the file system, fix the module's directory layout to match Maven's default layout.

    > That is:
    > 
    > * `/src/main/java/` for the main source files;
    > * `/src/main/resources/` for the main resource files;
    > * `/src/test/java/` for the test source files;
    > * `/src/test/resources/` for the test resource files;
    > * `/target/` for the compiler-generated files.

3. Move `/META-INF/plugin.xml` to the `/src/main/resources/` directory.

4. In IntelliJ's _Project Structure_ dialog on the _Sources_ tab of the module, fix the module's sources.

    > That is:
    >
    > * `/src/` is no longer a _Sources_ folder;
    > * `/src/main/java/` is a _Sources_ folder;
    > * `/src/main/resources/` is a _Resources_ folder;
    > * `/src/test/java/` is a _Tests_ folder;
    > * `/src/test/resources/` is a _Test Resources_ folder;
    > * `/target/` is an _Excluded_ folder.

5. On the _Plugin Deployment_ tab of the module, fix the _Path to META-INF/plugin.xml_ to point to the new location of the `META-INF/plugin.xml` file at `/src/main/resources/`.

    > You can see where IntelliJ expects the `plugin.xml` file to be in the `/my-plugin.iml` file.

6. Delete the `/META-INF/` directory from the root of the module.




## Add Maven support
Normally in IntelliJ you can change a module into a Maven module through the _Add Framework Support_ dialog from the module's context menu, but this option is not available for IntelliJ Plugin projects. We need a different approach. We're going to add the `pom.xml` manually, and let IntelliJ ignore it.

1. Outside IntelliJ, create a new `/pom.xml` file in the module's root, with the following content:

    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.mycompany.myplugin</groupId>
      <artifactId>my-plugin</artifactId>
      <version>0.1.0-alpha</version>
      
      <name>My Plugin</name>
      <description>My plugin for IntelliJ.</description>
    
      <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <ij.plugin>true</ij.plugin>
        <jdk.version>1.8</jdk.version>
      </properties>

    </project>
    ```
    
    > Adjust this file to your needs.

2. Go back to IntelliJ to the _Maven Projects_ window, and click the _..._ button. This will force IntelliJ to find any Maven modules in the project. However, since an IntelliJ Plugin can't be a Maven module, you'll get an error:

    > The following module had incorrect type and needs to be recreated:
    > 
    > IntelliJ Platform Plugin 'test-project' for Maven project com.mycompany.myplugin:my-plugin:0.1.0-alpha
    > 
    > If you do not want the module to be recreated, corresponding Maving projects will be marked as ignored, you will be able to unignore them afterwards in the Maven Projects tool window.

3. Click the _Ignore These Projects_ button. This adds the module's `pom.xml` file to the Maven ignore list. Now IntelliJ shouldn't try anymore to change the plugin into a Maven project.

    > You can find the files Maven ignores in the IntelliJ _Settings_ dialog, under _Build, Execution, Deployment_, _Build Tools_, _Maven_, _Ignored Files_.
    > 
    > Additionally, you can see the ignored files in the `/.idea/misc.xml` file. The paths are relative to the `$PROJECT_DIR$` variable.



## Setting up Maven for plugin development
Now that IntelliJ can build the plugin while ignoring Maven, we need to ensure Maven can build the plugin while ignoring IntelliJ.

### Installing IntelliJ SDK
Unfortunately there's no Maven artifact with the IntelliJ SDK JARs. However, we can install our own. You can choose to install it in a file repository next to the plugin, or in the local user repository.

1. Download the IntelliJ binary tar.gz distribution.

    > You probably already have it downloaded somewhere.

2. From the directory with the IntelliJ tar.gz, execute the following command to install it in your local Maven repository:

    ```
    mvn install:install-file -Dfile=ideaIC-14.1.4.tar.gz \
                             -DgroupId=org.jetbrains \
                             -DartifactId=org.jetbrains.intellij-ce \
                             -Dversion=14.1.4 \
                             -Dpackaging=tar.gz
    ```
    
    > **Note**: Change the command to suit your version of IntelliJ.

> Alternatively, you can install it in a file repository local to the project using the command:
>
> ```
> mvn install:install-file -Dfile=ideaIC-14.1.4.tar.gz \
>                          -DgroupId=org.jetbrains \
>                          -DartifactId=org.jetbrains.intellij-ce \
>                          -Dversion=14.1.4 \
>                          -Dpackaging=tar.gz \
>                          -DlocalRepositoryPath=./repository
> ```
> 
> Add the local repository directory (e.g. `/repository`) to the `.gitignore` file. You don't want to accidentally upload the whole IntelliJ distribution to your Git.
> 
> Add the following to the module's `/pom.xml` file (adjust as needed):
> 
> ```
> <repositories>
>   <repository>
>     <id>project-repo</id>
>     <url>file://${basedir}/../repository</url>
>   </repository>
> </repositories>
> ```



### Depend on the IntelliJ SDK

1. Add a dependency on the IntelliJ SDK to the module's `/pom.xml` file. Note that the dependency should have the `provided` scope, as the dependency is only needed at compile-time, but provided at run-time.

    ```
    <dependencies>
      <dependency>
        <groupId>org.jetbrains</groupId>
        <artifactId>org.jetbrains.intellij-ce</artifactId>
        <version>14.1.4</version>
        <type>tar.gz</type>
        <scope>provided</scope>
      </dependency>
    </dependencies>
    ```

2. Maven doesn't automatically add the contents of an archive to the class path, so we need to extract it. We only care about the JARs. Add the following to the `/pom.xml` file:

    ```
    <build>
      <plugins>
        <!-- ... -->
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-dependency-plugin</artifactId>
          <version>2.8</version>
          <executions>
            <execution>
              <id>unzip-distribution</id>
              <goals>
              <goal>unpack-dependencies</goal>
              </goals>
              <configuration>
                <includeArtifactIds>org.jetbrains.intellij-ce</includeArtifactIds>
                <outputDirectory>${project.build.directory}/dependency</outputDirectory>
                <includes>**/*.jar</includes>
              </configuration>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </build>
    ```
    
3. And we need to tell the compiler to look where we extracted those files.

    ```
    <build>
      <plugins>
        <!-- ... -->
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>3.2</version>
          <configuration>
            <source>${jdk.version}</source>
            <target>${jdk.version}</target>
            <compilerArguments>
              <extdirs>${project.build.directory}/dependency/${idea.foldername}/lib/</extdirs>
            </compilerArguments>
          </configuration>
        </plugin>
      </plugins>
    </build>
    ```
    
4. Note that we used the `idea.foldername` property. Add it to the `<properties>` tag of the `\pom.xml`. Adjust as needed.

    ```
    <properties>
      <!-- ... -->
      <idea.version>14.1.4</idea.version>
      <idea.foldername>idea-IC-141.1532.4</idea.foldername>
    </properties>
    ```

5. IntelliJ plugins have some Forms that also need to be compiled. Add this to your `/pom.xml`:
    
    ```
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-antrun-plugin</artifactId>
      <version>1.7</version>
      <executions>
        <execution>
          <id>compile-forms</id>
          <phase>process-classes</phase>
          <goals>
            <goal>run</goal>
          </goals>
          <configuration>
            <target>
              <path id="maven.plugin.complete.classpath">
                <path refid="maven.plugin.classpath" />
                <fileset dir="${project.build.directory}/dependency/${idea.foldername}/lib">
                 <include name="**/*.jar"/>
                </fileset>
              </path>
              <taskdef name="instrumentIdeaExtensions" classname="com.intellij.ant.InstrumentIdeaExtensions" classpathref="maven.plugin.complete.classpath" />
              <path id="sourcepath">
                <dirset dir="${project.basedir}">
                  <include name="src/main/java" />
                  <include name="src/main/resources" />
                </dirset>
              </path>
              <instrumentIdeaExtensions destdir="${project.build.outputDirectory}" extdirs="${project.build.directory}/dependency/${idea.foldername}/lib" includeantruntime="false">
                <classpath refid="maven.compile.classpath" />
                <src refid="sourcepath" />
              </instrumentIdeaExtensions>
            </target>
          </configuration>
        </execution>
      </executions>
      <dependencies>
        <dependency>
          <groupId>com.sun</groupId>
          <artifactId>tools</artifactId>
          <version>1.8.0</version>
          <scope>system</scope>
          <systemPath>${env.JAVA_HOME}/lib/tools.jar</systemPath>
        </dependency>
      </dependencies>
    </plugin>
    ```
    
    > **Note**: Here we use the `idea.foldername` property again.


### Resource filtering
There are some values that are shared between the pom.xml and the plugin.xml files. To make this easier, we can use resource filtering.

1. Enable resource filtering for the files in the `/src/main/resources` directory, by adding this to the `/pom.xml` file:

    ```
    <build>
      <resources>
        <resource>
          <directory>src/main/resources</directory>
          <filtering>true</filtering>
        </resource>
      </resources>
      
      <!-- ... -->
    </build>
    ```
    
2. Add some new properties to the `/pom.xml` file.
    
    ```
    <properties>
      <vendor.name>Mycompany</vendor.name>
      <vendor.url>http://www.my.com/</vendor.url>
      <vendor.email>me@my.com</vendor.email>
      <!-- ... -->
    </properties>
    ```

3. Replace many of the strings in the `/src/main/resources/META-INF/plugin.xml` file. For example:
    
    ```
    <idea-plugin version="2">
      <id>${project.artifactId}</id>
      <name>${project.name}</name>
      <version>${project.version}</version>
      <vendor email="${vendor.email}" url="${vendor.url}">${vendor.name}</vendor>
    
      <description><![CDATA[
        ${project.description}
      ]]></description>
    
      <change-notes><![CDATA[
        Bug-fixes and improvements.
      ]]>
      </change-notes>
    
      <idea-version since-build="131"/>
    
      <!-- ... -->
    </idea-plugin>
    ```
    
4. The `process-resources` phase will filter the resources, but unfortunately will also unzip the IntelliJ distribution each time it is run. So we need to add a new profile that skips the unzipping. Add this to your `/pom.xml`:
    
    ```
    <profiles>
      <profile>
        <id>dev</id>
        <build>
          <plugins>
            <plugin>
              <groupId>org.apache.maven.plugins</groupId>
              <artifactId>maven-dependency-plugin</artifactId>
              <executions>
                <execution>
                  <id>unzip-distribution</id>
                  <phase>none</phase>
                </execution>
              </executions>
            </plugin>
          </plugins>
        </build>
      </profile>
    </profiles>
    ```

5. Finally, we need to convince IntelliJ to run the `process-resources` Maven phase before _Make_. In IntelliJ, go to _Run_ &rarr; _Edit Configurations_. Edit the Plugin configuration by adding a _Run Maven Goal_ step before _Make_. Select the project's root as the working directory, and as the command line:
    
    ```
    process-resources -Pdev
    ```
    
## Final POM
The final `pom.xml` file that we created can be found in [this Gist](https://gist.github.com/Virtlink/7a7f7893db9e55ebb489).


## Extra

### Adding SDK sources
The source files are invaluable for debugging and understanding the IntelliJ SDK. Let's install them.

> **Note**: The source files may already have been installed.

1. Download the source tarball corresponding to your IntelliJ version from [this download page](http://www.jetbrains.org/display/IJOS/Download). For example

    ```
    wget http://download.jetbrains.com/idea/ideaIC-14.1.4-src.tar.bz2
    ```
    > **Note**: The source files are approximately 345 MB.
    >
    > Alternatively, you might clone the IntelliJ Git repository [https://github.com/JetBrains/intellij-community.git](https://github.com/JetBrains/intellij-community).

2. Extract the source files somewhere.

3. In IntelliJ, go to the _Project Structure_ dialog, _SDKs_. Select the _IntelliJ SDK_, and go to the _Sourcepath_ tab.

4. Add the extracted source files to the source path. 



## Notes

### Depending on a plugin
Your plugin might depend on a third-party plugin. In this case, specify the third-party plugin as a `provided` dependency. In IntelliJ, add the JARs of the third-party plugin to the _SDK classpath_, not as a library to the module.

### Provided dependencies
Some dependencies may already be present in IntelliJ (such as `slf4j-api`) or a third-party plugin on which you depend. Such dependencies should have the `provided` scope, and in IntelliJ you should remove those (transitive) dependencies from the _Project Structure_.

If necessary, you can remove some transitive dependencies in Maven 3 by adapting this example:

```
<dependencies>
  <dependency>
    <groupId>org.example</groupId>
    <artifactId>my-dependency</artifactId>
    <version>1.0</version>
    <exclusions>
      <exclusion>
        <groupId>*</groupId>
        <artifactId>*<artifactId>
      </exclusion>
    </exclusions>
  </dependency>
</dependencies>
```


## See also

* [How to manage development life cycle of IntelliJ plugins with Maven](<http://labs.bsb.com/2013/11/how-to-manage-development-life-cycle-of-intellij-plugins-with-maven-2/>) by Jacques Gauthier
