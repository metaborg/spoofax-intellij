package org.metaborg.spoofax.intellij;

import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import org.apache.commons.vfs2.FileSystemManager;
import org.metaborg.core.project.settings.IProjectSettingsService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.core.project.settings.ISpoofaxProjectSettingsService;
import org.metaborg.spoofax.intellij.languages.LanguageManager;
import org.metaborg.spoofax.intellij.logging.Slf4JTypeListener;
import org.metaborg.spoofax.intellij.project.settings.IProjectSettingsService2;
import org.metaborg.spoofax.intellij.project.settings.IntelliJProjectSettingsService;
import org.metaborg.spoofax.intellij.project.settings.IntelliJSpoofaxProjectSettingsService;
import org.metaborg.spoofax.intellij.project.settings.YamlProjectSettingsService;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.intellij.resources.IntelliJFileSystemManagerProvider;
import org.metaborg.spoofax.intellij.resources.IntelliJResourceService;

/**
 * The Guice dependency injection module for the Spoofax IntelliJ plugins.
 */
public abstract class SpoofaxIntelliJDependencyModule extends SpoofaxModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bind(LanguageManager.class).in(Singleton.class);

        bindListener(Matchers.any(), new Slf4JTypeListener());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindProjectSettings() {
        bind(IProjectSettingsService2.class).to(YamlProjectSettingsService.class).in(Singleton.class);
        bind(IProjectSettingsService.class).to(IntelliJProjectSettingsService.class).in(Singleton.class);
        bind(ISpoofaxProjectSettingsService.class).to(IntelliJSpoofaxProjectSettingsService.class).in(Singleton.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindResource() {
        bind(IntelliJResourceService.class).in(Singleton.class);
        bind(IResourceService.class).to(IntelliJResourceService.class).in(Singleton.class);
        bind(IIntelliJResourceService.class).to(IntelliJResourceService.class).in(Singleton.class);
        bind(FileSystemManager.class).toProvider(IntelliJFileSystemManagerProvider.class).in(Singleton.class);
    }
}
