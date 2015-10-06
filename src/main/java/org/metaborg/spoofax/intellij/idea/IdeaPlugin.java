package org.metaborg.spoofax.intellij.idea;

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

    private ILanguageDiscoveryService languageDiscoveryService;
    private IResourceService resourceService;
    private ILanguageService languageService;

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
    private final void inject(@NotNull final ILanguageService languageService,
                              @NotNull final ILanguageDiscoveryService languageDiscoveryService,
                              @NotNull final IResourceService resourceService) {
        this.languageDiscoveryService = languageDiscoveryService;
        this.resourceService = resourceService;
        this.languageService = languageService;
    }

    public final void initComponent() {

        System.out.println();
        // IdeaLanguageManager

    }

    public final void disposeComponent() {

    }

    @NotNull
    public String getComponentName() {
        return IdeaPlugin.class.getName();
    }

}
