package org.metaborg.spoofax.intellij.meta;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IntelliJ IDEA meta languages plugin class.
 */
public final class IdeaMetaPlugin implements ApplicationComponent {

    final Logger logger = LoggerFactory.getLogger(IdeaMetaPlugin.class);

    protected static Injector injector = null;
    private MetaLanguageManager metaLanguageManager;

    /**
     * Gets the injector.
     * @return The current injector.
     */
    public static Injector injector() {
        assert injector != null;

        return injector;
    }

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public IdeaMetaPlugin() { }

    @Inject
    @SuppressWarnings("unused")
    private void inject(MetaLanguageManager metaLanguageManager) {
        this.metaLanguageManager = metaLanguageManager;
    }

    @Override
    public void initComponent() {
        injector = IdeaPlugin.injector().createChildInjector(new SpoofaxIdeaMetaDependencyModule());
        IdeaMetaPlugin.injector().injectMembers(this);

        logger.info("Loaded Meta plugin!");
        logger.info("Loading Meta languages...");
        this.metaLanguageManager.loadLanguages();
        logger.info("Meta languages loaded!");
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return IdeaMetaPlugin.class.getName();
    }
}
