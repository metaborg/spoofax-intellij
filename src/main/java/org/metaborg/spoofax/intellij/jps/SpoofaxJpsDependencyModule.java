package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.core.project.IProjectService;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;
import org.metaborg.spoofax.intellij.jps.targetbuilders.*;
import org.metaborg.spoofax.meta.core.ant.AntRunnerService;
import org.metaborg.spoofax.meta.core.ant.IAntRunnerService;

import java.util.Arrays;
import java.util.Collection;

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

        bind(SpoofaxPreTargetType.class).in(Singleton.class);
        bind(SpoofaxPostTargetType.class).in(Singleton.class);
        bind(SpoofaxSourceGenBuilder.class).in(Singleton.class);

        bind(SpoofaxPreBuilder.class).in(Singleton.class);
        bind(SpoofaxPostBuilder.class).in(Singleton.class);

        bind(IJpsProjectService.class).to(JpsProjectService.class).in(Singleton.class);


        bind(IAntRunnerService.class).to(AntRunnerService.class).in(Singleton.class);
    }

    @Singleton
    @Provides
    @Inject
    public Collection<BuildTargetType<?>> provideTargetTypes(SpoofaxPreTargetType preTargetType, SpoofaxPostTargetType postTargetType) {
        return Arrays.asList(preTargetType, postTargetType);
    }

    @Singleton
    @Provides
    @Inject
    public Collection<TargetBuilder<?, ?>> provideTargetBuilders(SpoofaxPreBuilder preBuilder, SpoofaxPostBuilder postBuilder) {
        return Arrays.asList(preBuilder, postBuilder);
    }

    @Singleton
    @Provides
    @Inject
    public Collection<ModuleLevelBuilder> provideModuleLevelBuilders(SpoofaxSourceGenBuilder sourceGenBuilder) {
        return Arrays.asList(sourceGenBuilder);
    }

    @Override
    protected void bindProject() {
        bind(IProjectService.class).to(JpsProjectService.class).in(Singleton.class);
    }
}

