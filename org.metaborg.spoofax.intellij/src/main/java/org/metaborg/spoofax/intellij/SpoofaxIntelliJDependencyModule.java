package org.metaborg.spoofax.intellij;

import com.google.inject.Singleton;
import org.metaborg.spoofax.core.SpoofaxModule;

/**
 * The Guice dependency injection module for the Spoofax IntelliJ plugins.
 */
public abstract class SpoofaxIntelliJDependencyModule extends SpoofaxModule {

    @Override
    protected void configure() {
        super.configure();

        bindBuildTargets();
    }

    protected void bindBuildTargets() {
        bind(SpoofaxProductionTargetType.class).in(Singleton.class);
        bind(JpsSpoofaxModuleType.class).in(Singleton.class);
    }
}
