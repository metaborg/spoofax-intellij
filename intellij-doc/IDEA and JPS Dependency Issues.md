# IDEA and JPS Dependency Issues
An IntelliJ IDEA plugin should not want to load all its dependencies when they are already provided by the IDE (e.g. `slf4j-api`) or plugin dependencies (e.g. Metaborg Core). However, IntelliJ copies all (transitive) dependencies of the plugin to the plugin's `lib` directory (at `~/.IdeaIC14/system/plugins-sandbox/plugins/org.metaborg.spoofax.intellij.idea/lib`), and loads any and all JARs that are in that `lib` directory. This is an issue when the IDEA plugin depends on the JPS plugin, and the IDEA plugin should _not_ load `slf4j-api` whereas the JPS plugin _should_ load `slf4j-api`.

A crappy solution is to create a `/src/main/resources/jpslib/` directory in the IDEA plugin project, and put there those JARs that the IDEA plugin should not load but the JPS plugin should. In my case, I put `slf4j-api-1.7.12.jar` and `alf4j-simple-1.7.12.jar` there. Then in the IDEA plugin's `plugin.xml` file, in the `compileServer.plugin` element, I prefixed only those JPS dependencies with `../classes/jpslib/`. That works for now. For example:

```
<compileServer.plugin classpath="my-jps.jar;../classes/jpslib/slf4j-api.jar;../classes/jpslib/slf4j-simple;org.metaborg.core.jar"/>
```

And in the JPS plugin, I added those JARs as `provided` JARs.
