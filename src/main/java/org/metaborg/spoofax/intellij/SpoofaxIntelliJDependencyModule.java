package org.metaborg.spoofax.intellij;

import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileSystemManager;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.core.project.IMavenProjectService;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.intellij.resources.IntelliJFileSystemManagerProvider;
import org.metaborg.spoofax.intellij.resources.IntelliJResourceService;

/**
 * The Guice dependency injection module for the Spoofax IntelliJ plugins.
 */
public abstract class SpoofaxIntelliJDependencyModule extends SpoofaxModule {

    @Override
    protected void configure() {
        super.configure();

        bindBuildTargets();

        bind(LanguageManager.class).in(Singleton.class);
    }

    @Override
    protected void bindMavenProject() {
        bind(IMavenProjectService.class).to(IntelliJMavenProjectService.class).in(Singleton.class);
    }

    @Override
    protected void bindResource() {
        bind(IntelliJResourceService.class).in(Singleton.class);
        bind(IResourceService.class).to(IntelliJResourceService.class);
        bind(IIntelliJResourceService.class).to(IntelliJResourceService.class);
        bind(FileSystemManager.class).toProvider(IntelliJFileSystemManagerProvider.class).in(Singleton.class);
    }

    protected void bindBuildTargets() {

    }
}
