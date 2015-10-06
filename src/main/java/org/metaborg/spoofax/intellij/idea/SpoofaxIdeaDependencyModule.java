package org.metaborg.spoofax.intellij.idea;

import com.google.inject.Singleton;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxModuleBuilder;
import org.metaborg.spoofax.intellij.languages.IdeaLanguageManager;

/**
 * The Guice dependency injection module for the Spoofax IDEA plugin.
 */
public final class SpoofaxIdeaDependencyModule extends SpoofaxIntelliJDependencyModule {

    @Override
    protected void configure() {
        super.configure();

        bind(SpoofaxModuleBuilder.class).in(Singleton.class);
        bind(IdeaLanguageManager.class).in(Singleton.class);
    }

}
