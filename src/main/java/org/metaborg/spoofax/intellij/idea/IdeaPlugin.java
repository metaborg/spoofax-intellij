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
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.spoofax.intellij.serialization.SpoofaxGlobalService;
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
    public static Injector injector() {
        return injector.get();
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(@NotNull ILanguageService languageService,
                        @NotNull ILanguageDiscoveryService languageDiscoveryService,
                        @NotNull IResourceService resourceService) {
        this.languageDiscoveryService = languageDiscoveryService;
        this.resourceService = resourceService;
        this.languageService = languageService;
    }

    public void initComponent() {
//        try {
//            loadLanguages();
//        } catch (MetaborgException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * Loads languages.
     */
    private void loadLanguages() throws MetaborgException {
        FileObject location = null;
        try {
            location = resourceService.resolve(
                    "zip:///home/daniel/repos/metaborg-spoofax-intellij/meta/sdf.spoofax-language");

            if (location == null || !location.exists())
                return;
        } catch (MetaborgRuntimeException e) {
            e.printStackTrace();
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        if (location == null)
            return;
        final Iterable<ILanguageComponent> languageComponents = languageDiscoveryService.discover(location);

        System.out.println(this.languageService.getAllLanguages());

        SpoofaxGlobalService.getInstance().getState().setMyName("test name!");
    }

    public void disposeComponent() {

    }

    @NotNull
    public String getComponentName() {
        return IdeaPlugin.class.getName();
    }

}
