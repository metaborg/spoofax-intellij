package org.metaborg.spoofax.intellij.idea;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intellij.openapi.components.ApplicationComponent;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.intellij.SpoofaxProductionTargetType;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IntelliJ IDEA plugin class.
 */
public final class IdeaPlugin implements ApplicationComponent {

    final Logger logger = LoggerFactory.getLogger(IdeaPlugin.class);

    protected static final Supplier<Injector> injector = Suppliers.memoize(() -> Guice.createInjector(new SpoofaxIdeaDependencyModule()));

    /**
     * Gets the injector.
     * @return The current injector.
     */
    public static Injector injector() {
        return injector.get();
    }

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

    @Inject @SuppressWarnings("unused")
    private void inject(ILanguageService languageService, ILanguageDiscoveryService languageDiscoveryService, IResourceService resourceService) {
        this.languageDiscoveryService = languageDiscoveryService;
        this.resourceService = resourceService;
        this.languageService = languageService;
    }

    public void initComponent() {
        try {
            loadLanguages();
        } catch (MetaborgException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads languages.
     */
    private void loadLanguages() throws MetaborgException {
        FileObject location = resourceService.resolve("zip:///home/daniel/repos/metaborg-spoofax-intellij/meta/sdf.spoofax-language");
        Iterable<ILanguageComponent> languageComponents = languageDiscoveryService.discover(location);

        System.out.println(this.languageService.getAllLanguages());

        //System.out.println(languageComponents.iterator().next().contributesTo());
    }

    public void disposeComponent() {

    }

    @NotNull
    public String getComponentName() {
        return IdeaPlugin.class.getName();
    }

}
