package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.metaborg.core.project.IProjectService;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.SpoofaxPostTargetType;
import org.metaborg.spoofax.intellij.SpoofaxPreTargetType;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepProvider;
import org.metaborg.spoofax.intellij.jps.builders.SpoofaxBuildStepProvider;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;

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

        bind(SpoofaxPreBuilder.class).in(Singleton.class);
        bind(SpoofaxPostBuilder.class).in(Singleton.class);


//        Multibinder<SpoofaxTargetType<?>> targetTypes = Multibinder.newSetBinder(this.binder(), new TypeLiteral<SpoofaxTargetType<?>>() {
//        });
//        targetTypes.addBinding().to(SpoofaxPreTargetType.class).in(Singleton.class);
//        targetTypes.addBinding().to(SpoofaxPostTargetType.class).in(Singleton.class);

//        Multibinder<SpoofaxOldBuilder> builders = Multibinder.newSetBinder(this.binder(), SpoofaxOldBuilder.class);
//        builders.addBinding().to(SpoofaxOldBuilder.class).in(Singleton.class);

        bind(IBuildStepProvider.class).to(SpoofaxBuildStepProvider.class).in(Singleton.class);

        //this.bind(JpsModule.class).toInstance(this.module);

        //bind(new TypeLiteral<SpoofaxTargetType<?>>).
        //bindProjectService();
        bindTargetBuilder();
    }

    @Singleton
    @Provides
    @Inject
    public Collection<SpoofaxTargetType<?>> provideTargetTypes(SpoofaxPreTargetType preTargetType, SpoofaxPostTargetType postTargetType) {
        return Arrays.asList(preTargetType, postTargetType);
    }

    @Singleton
    @Provides
    @Inject
    public Collection<SpoofaxBuilder<?>> provideSpoofaxBuilders(SpoofaxPreBuilder preBuilder, SpoofaxPostBuilder postBuilder) {
        return Arrays.asList(preBuilder, postBuilder);
    }

    private void bindTargetBuilder() {
//        bind(SpoofaxOldBuilder.class).in(Singleton.class);
    }

    @Override
    protected void bindProject() {
        bind(IProjectService.class).to(JpsProjectService.class).in(Singleton.class);
    }
}

