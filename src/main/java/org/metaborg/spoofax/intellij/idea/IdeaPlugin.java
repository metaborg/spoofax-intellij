package org.metaborg.spoofax.intellij.idea;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.languages.LanguageManager;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

/**
 * IntelliJ IDEA plugin class.
 */
public final class IdeaPlugin implements ApplicationComponent {

    @NotNull
    protected static final Supplier<Injector> injector = Suppliers.memoize(() -> Guice.createInjector(new SpoofaxIdeaDependencyModule()));
    @InjectLogger
    private Logger logger;
    @NotNull
    private LanguageManager languageManager;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public IdeaPlugin() {
        IdeaPlugin.injector().injectMembers(this);
    }

    /**
     * Gets the injector.
     *
     * @return The current injector.
     */
    @NotNull
    public final static Injector injector() {
        return injector.get();
    }

    @Inject
    @SuppressWarnings("unused")
    private final void inject(@NotNull final LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    /**
     * Initializes the plugin.
     */
    public final void initComponent() {

        this.languageManager.loadMetaLanguages();
    }

    /**
     * Disposes the plugin.
     */
    public final void disposeComponent() {

    }

    /**
     * Gets the name of the application component.
     *
     * @return The name of the component.
     */
    @NotNull
    public String getComponentName() {
        return IdeaPlugin.class.getName();
    }

}
