package org.metaborg.spoofax.intellij.idea;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intellij.openapi.components.ApplicationComponent;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.intellij.SpoofaxProductionTargetType;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;

/**
 * IntelliJ IDEA plugin class.
 */
public final class IdeaPlugin implements ApplicationComponent {

    protected static final Supplier<Injector> injector = Suppliers.memoize(() -> Guice.createInjector(new SpoofaxIdeaDependencyModule()));

    private ILanguageDiscoveryService languageDiscoveryService;
    private IResourceService resourceService;

    // FIXME: Is there a way to automatically inject dependencies into object
    // that were not created by Guice?
    /**
     * Injects the members of an existing object.
     *
     * Generally this method should not be used except in exceptional circumstances
     * where the object is not constructed by Guice. Usage example:
     *
     * <pre>
     * {@code
     * public class SpoofaxBuildTargetScopeProvider extends BuildTargetScopeProvider {
     *
     *     private SpoofaxTargetType targetType;
     *
     *     // Constructor called by IntelliJ.
     *     public SpoofaxBuildTargetScopeProvider() {
     *         IdeaPlugin.injectMembers(this);
     *     }
     *
     *     @Inject @SuppressWarnings("unused")
     *     private void inject(SpoofaxTargetType targetType) {
     *         this.targetType = targetType;
     *     }
     * }
     * }
     * </pre>
     *
     * @param o The object whose members need to be injected.
     */
    public static void injectMembers(Object o) {
        injector.get().injectMembers(o);
    }

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public IdeaPlugin() {
        IdeaPlugin.injectMembers(this);
    }

    @Inject @SuppressWarnings("unused")
    private void inject(ILanguageDiscoveryService languageDiscoveryService, IResourceService resourceService) {
        this.languageDiscoveryService = languageDiscoveryService;
        this.resourceService = resourceService;
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
        FileObject location = resourceService.resolve("file:///home/daniel/repos/metaborg-spoofax-intellij/meta/sdf.spoofax-language");
        Iterable<ILanguageComponent> languageComponents = languageDiscoveryService.discover(location);
        System.out.println(languageComponents);
    }

    public void disposeComponent() {

    }

    @NotNull
    public String getComponentName() {
        return null;
    }

}
