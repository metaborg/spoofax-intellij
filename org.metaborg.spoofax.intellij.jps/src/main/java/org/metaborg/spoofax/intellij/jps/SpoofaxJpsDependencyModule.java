package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Singleton;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;

/**
 * The Guice dependency injection module for the Spoofax JPS plugin.
 */
public final class SpoofaxJpsDependencyModule extends SpoofaxIntelliJDependencyModule {

    @Override
    protected void configure() {
        super.configure();

        bindTargetBuilder();
    }

    private void bindTargetBuilder() {
        bind(SpoofaxTargetBuilder.class).in(Singleton.class);
    }
}
