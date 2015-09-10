package org.metaborg.spoofax.intellij.idea;

import com.google.inject.Singleton;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;

/**
 * The Guice dependency injection module for the Spoofax IDEA plugin.
 */
public final class SpoofaxIdeaDependencyModule extends SpoofaxIntelliJDependencyModule {

    @Override
    protected void configure() {
        super.configure();
    }

}
