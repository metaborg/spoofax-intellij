package org.metaborg.spoofax.intellij.factories;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for service loader providers.
 *
 * Usage:
 * <pre>{@code
 * class MyModule extends AbstractModule {
 *
 *     @Override
 *     protected void configure() {
 *         // ...
 *         install(new JavaServiceProviderFactory().provide(IMyInterface.class));
 *
 *     }
 *
 * }
 * }</pre>
 */
public final class JavaServiceProviderFactory {

    /**
     * {@inheritDoc}
     */
    public <T> Module provide(@NotNull final Class<T> service) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                Provider<T> provider = new JavaServiceProvider<T>(service);
                bind(service).toProvider(provider);
            }
        };
    }
}
