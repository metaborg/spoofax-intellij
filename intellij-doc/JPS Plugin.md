# JPS Plugin
The _JPS plugin_ is a separate module that uses the JetBrains Project System (JPS) to build IntelliJ modules.

## Implementation

To create a JPS plugin, you need three modules:

1. _IDEA_: The IntelliJ IDEA plugin module.
2. _JPS_: The JPS module.
3. _Common_: A Java module with classes common to both the IDEA and JPS modules.

The IDEA module depends on the JPS module, and both the IDEA and JPS modules depend on the Common module. Add any other dependencies as needed.

### Common module
Implement a `BuildRootDescriptor`, for example:

```
public class SpoofaxSourceRootDescriptor extends BuildRootDescriptor {

    private final File root;
    private final SpoofaxBuildTarget target;

    public SpoofaxSourceRootDescriptor(File root, SpoofaxBuildTarget target)
    {
        this.root = root;
        this.target = target;
    }

    @Override
    public String getRootId() {
        return "SpoofaxSourceRootDescriptor";
    }

    @Override
    public File getRootFile() {
        return this.root;
    }

    @Override
    public BuildTarget<?> getTarget() {
        return this.target;
    }
}
```

Now you need to implement a build target. For example:

```
public class SpoofaxBuildTarget extends BuildTarget<SpoofaxSourceRootDescriptor> {

    public static final String TARGET_ID = "spoofax_build_target";

    public SpoofaxBuildTarget() {
        super(SpoofaxBuildTargetType.INSTANCE);
    }

    @Override
    public String getId() {
        return TARGET_ID;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax Target";
    }

    @Override
    public Collection<BuildTarget<?>> computeDependencies(BuildTargetRegistry targetRegistry, TargetOutputIndex outputIndex) {
        return Collections.emptyList();
    }

    @NotNull
    @Override
    public List<SpoofaxSourceRootDescriptor> computeRootDescriptors(JpsModel model, ModuleExcludeIndex index, IgnoredFileIndex ignoredFileIndex, BuildDataPaths dataPaths) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public SpoofaxSourceRootDescriptor findRootDescriptor(String rootId, BuildRootIndex rootIndex) {
        return null;
    }

    @NotNull
    @Override
    public Collection<File> getOutputRoots(CompileContext context) {
        return Collections.emptyList();
    }
}
```

Finally, you need to implement a `BuildTargetType` for the new build target.

```
public class SpoofaxBuildTargetType extends BuildTargetType<SpoofaxBuildTarget> {

    public static final String TARGET_TYPE_ID = "spoofax_build_target_type";

    public static SpoofaxBuildTargetType INSTANCE = new SpoofaxBuildTargetType();

    protected SpoofaxBuildTargetType() {
        super(TARGET_TYPE_ID);
    }

    @Nullable
    public SpoofaxBuildTarget getBuildTarget(JpsModel model)
    {
        return new SpoofaxBuildTarget();
    }

    @NotNull
    @Override
    public List<SpoofaxBuildTarget> computeAllTargets(JpsModel model) {
        SpoofaxBuildTarget target = getBuildTarget(model);
        return ContainerUtil.createMaybeSingletonList(target);
    }

    @NotNull
    @Override
    public BuildTargetLoader<SpoofaxBuildTarget> createLoader(@NotNull final JpsModel model) {
        return new BuildTargetLoader<SpoofaxBuildTarget>() {
            @Nullable
            @Override
            public SpoofaxBuildTarget createTarget(@NotNull String targetId) {
                return getBuildTarget(model);
            }
        };
    }
}
```

### IDEA module
The IDEA module (the actual IntelliJ plugin module) must be made aware of the existence of the JPS module. First, you need to implement a `BuildTargetScopeProvider` to tell IntelliJ about the new build target type you added to the Common module. For example:

```
public class SpoofaxBuildTargetScopeProvider extends BuildTargetScopeProvider {

    @NotNull @Override
    public List<CmdlineRemoteProto.Message.ControllerMessage.ParametersMessage.TargetTypeBuildScope> getBuildTargetScopes(
      CompileScope baseScope, CompilerFilter filter, Project project, boolean forceBuild) {
        return Collections.singletonList(CmdlineProtoUtil.createAllTargetsScope(SpoofaxBuildTargetType.INSTANCE, forceBuild));
    }
}
```

Register the scope provider as a plugin extension in the `META-INF/plugin.xml` file, and adjust the file as needed:

```
<extensions defaultExtensionNs="com.intellij">
  <compiler.buildTargetScopeProvider implementation="SpoofaxBuildTargetScopeProvider"/>
</extensions>
```

You also need to tell the IDEA module about the JPS module, like this:

```
<extensions defaultExtensionNs="com.intellij">
  <compileServer.plugin classpath="org.metaborg.spoofax.intellij.jps.jar"/>
</extensions>
```

### JPS module
In the JPS module you need to implement a `TargetBuilder`:

```
public class SpoofaxTargetBuilder extends TargetBuilder<SpoofaxSourceRootDescriptor, SpoofaxBuildTarget> {

    protected SpoofaxTargetBuilder() {
        super(Collections.singletonList(SpoofaxBuildTargetType.INSTANCE));
    }

    @Override
    public void buildStarted(CompileContext context) {
        JavaBuilder.IS_ENABLED.set(context, Boolean.FALSE);
    }

    @Override
    public void build(SpoofaxBuildTarget target, DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxBuildTarget> holder, BuildOutputConsumer outputConsumer, CompileContext context) throws ProjectBuildException, IOException {
        context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.WARNING, "Compilation not implemented!"));
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax Target Builder";
    }
}
```

And implement a `BuilderService` to return the target builder:

```
public final class SpoofaxBuilderService extends BuilderService {

    @NotNull
    @Override
    public List<? extends BuildTargetType<?>> getTargetTypes() {
        return Collections.singletonList(SpoofaxBuildTargetType.INSTANCE);
    }

    @NotNull
    @Override
    public List<? extends TargetBuilder<?,?>> createBuilders() {
        return Collections.singletonList(new SpoofaxTargetBuilder());
    }
}
```

Finally, you need to register the builder service with the JPS plugin. To do this, add a file named `org.jetbrains.jps.incremental.BuilderService` (it has no extension) to the `META-INF/services` directory of the JPS module. In it you put the fully qualified name of the builder service, for example:

```
org.metaborg.spoofax.intellij.jps.targetbuilders.SpoofaxBuilderService
```

## Debugging
Create a new Plugin run configuration that runs the IDEA plugin, e.g. `IDEA Plugin`. You can use that configuration to run or debug the IDEA plugin.

To debug the JPS plugin, you need two additional configurations. Duplicate the Plugin run configuration and give it a unique name. e.g. `IDEA Plugin (Debug JPS)`. In the _VM Options_ fields of the run configuration, add the following option to the existing list of options:

```
-Dcompiler.process.debug.port=5005
```

This tells IntelliJ to wait for the debugger to connect to port 5005 before running the JPS module.

Create a new Remote run configuration. By default it's configured to connect to port 5005.

To debug the JPS module, first run (not debug) the IDE using the new Plugin run configuration. Then in the IDE, click _Make Project_. You'll notice IntelliJ waiting for something. Now is the time to connect the debugger. Select the Remote run configuration and click debug. Now you can debug the JPS module, set breakpoints, etc.

## Class path
In the `compileServer.plugin` extension you have to specify the classpath of the JPS plugin. However, you need to include any and all (transitive) dependencies of the JPS plugin, separated by semi-colons (`;`).

When running or debugging a plugin, a new instance of IntelliJ is started. The plugins loaded in that new instance are found in `~/.IdeaIC14/system/plugins-sandbox/plugins/my-plugin`.
The subdirectory `classes` contains the compiled class files of your plugin, including any module dependencies (Common and JPS). The `lib` subdirectory contains the transitive dependencies of your plugin, which is where JPS will look for the files you specify on the class path.
