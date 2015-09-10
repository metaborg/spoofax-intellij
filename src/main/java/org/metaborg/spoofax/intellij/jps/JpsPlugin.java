package org.metaborg.spoofax.intellij.jps;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * JPS plugin class.
 */
public final class JpsPlugin {

    protected static final Supplier<Injector> injector = Suppliers.memoize(() -> Guice.createInjector(new SpoofaxJpsDependencyModule()));

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
     *         SpoofaxPlugin.injectMembers(this);
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

    private JpsPlugin() { }
}
