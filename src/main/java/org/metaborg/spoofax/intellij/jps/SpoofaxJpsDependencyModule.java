package org.metaborg.spoofax.intellij.jps;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProjectService;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;

/**
 * The Guice dependency injection module for the Spoofax JPS plugin.
 */
public final class SpoofaxJpsDependencyModule extends SpoofaxIntelliJDependencyModule {

    private final JpsModule module;

    /**
     * Initializes a new instance of the {@link SpoofaxJpsDependencyModule} class.
     * @param module The JPS module.
     */
    public SpoofaxJpsDependencyModule(JpsModule module) {
        Preconditions.checkNotNull(module);

        this.module = module;
    }

    @Override
    protected void configure() {
        super.configure();

        this.bind(JpsModule.class).toInstance(this.module);


        //bindProjectService();
        bindTargetBuilder();
    }

    private void bindTargetBuilder() {
        bind(SpoofaxBuilder.class).in(Singleton.class);
    }

    @Override
    protected void bindProject() {
        bind(IProjectService.class).to(JpsProjectService.class).in(Singleton.class);
    }
}
