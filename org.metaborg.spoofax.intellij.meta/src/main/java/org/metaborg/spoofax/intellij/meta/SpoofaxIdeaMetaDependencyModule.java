package org.metaborg.spoofax.intellij.meta;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.metaborg.spoofax.intellij.SpoofaxIntelliJDependencyModule;

/**
 * The Guice dependency injection module for the Spoofax IDEA meta languages plugin.
 */
public final class SpoofaxIdeaMetaDependencyModule extends AbstractModule {

    @Override
    protected void configure() {
        bindLanguageManager();
    }

    private void bindLanguageManager() {
        this.bind(MetaLanguageManager.class).in(Singleton.class);
    }

}
