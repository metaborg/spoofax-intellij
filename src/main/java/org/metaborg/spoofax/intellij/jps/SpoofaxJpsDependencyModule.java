package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.core.project.IProjectService;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.jps.ant.DefaultAntRunnerService;
import org.metaborg.spoofax.intellij.jps.builders.*;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;
import org.metaborg.spoofax.intellij.jps.targets.*;
import org.metaborg.spoofax.meta.core.SpoofaxMetaBuilder;
import org.metaborg.spoofax.meta.core.ant.IAntRunnerService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * The Guice dependency injection module for the Spoofax JPS plugin.
 */
public final class SpoofaxJpsDependencyModule extends SpoofaxIntelliJDependencyModule {

    //private final JpsModule module;

    /**
     * Initializes a new instance of the {@link SpoofaxJpsDependencyModule} class.
     */
    public SpoofaxJpsDependencyModule() { //JpsModule module) {
        //Preconditions.checkNotNull(module);

        //this.module = module;
    }

    @Override
    protected void configure() {
        super.configure();

        bind(SpoofaxNewPreTargetType.class).in(Singleton.class);
        bind(SpoofaxNewPostTargetType.class).in(Singleton.class);
        bind(SpoofaxSourceGenBuilder.class).in(Singleton.class);

        bind(SpoofaxNewBuilder.class).in(Singleton.class);

        bind(IJpsProjectService.class).to(JpsProjectService.class).in(Singleton.class);

        bind(IBuildStepProvider.class).to(SpoofaxBuildStepProvider.class).in(Singleton.class);

        bind(InitializationBuildStep.InitializationBuildStepDescriptor.class).in(Singleton.class);
        bind(IntellijJavaBuildStep.IntellijJavaBuildStepDescriptor.class).in(Singleton.class);

        bind(IAntRunnerService.class).to(DefaultAntRunnerService.class).in(Singleton.class);
    }

    @Singleton
    @Provides
    @Inject
    public Collection<BuildTargetType<?>> provideTargetTypes(SpoofaxNewPreTargetType preTargetType, SpoofaxNewPostTargetType postTargetType) {
        return Arrays.asList(preTargetType, postTargetType);
    }

    @Singleton
    @Provides
    @Inject
    public Collection<TargetBuilder<?, ?>> provideTargetBuilders(SpoofaxNewBuilder builder) {
        return Arrays.asList(builder);
    }

    @Singleton
    @Provides
    @Inject
    public Collection<ModuleLevelBuilder> provideModuleLevelBuilders(SpoofaxSourceGenBuilder sourceGenBuilder) {
        return Arrays.asList(sourceGenBuilder);
    }

    @Singleton
    @Provides
    @Inject
    public Collection<IBuildStepDescriptor> provideRootBuildStepDescriptors() {
        return Arrays.asList();
    }

    @Override
    protected void bindProject() {
        bind(IProjectService.class).to(JpsProjectService.class).in(Singleton.class);
    }
}

