package org.metaborg.spoofax.intellij.jps;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jetbrains.annotations.NotNull;

/**
 * JPS plugin class.
 */
public final class JpsPlugin {

    @NotNull
    private static final Supplier<Injector> injector = Suppliers.memoize(() -> Guice.createInjector(new SpoofaxJpsDependencyModule()));

    private JpsPlugin() {
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

}
