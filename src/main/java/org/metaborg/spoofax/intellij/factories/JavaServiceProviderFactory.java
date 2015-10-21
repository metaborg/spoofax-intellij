package org.metaborg.spoofax.intellij.factories;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for service loader providers.
 * <p>
 * Usage:
 * <pre>
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
 * </pre>
 */
public final class JavaServiceProviderFactory {

    /**
     * Creates a provider for a Java service.
     *
     * @param serviceClass The service class.
     * @return A {@link Module} that binds the provider.
     */
    public <T> Module provide(@NotNull final Class<T> serviceClass) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                Provider<T> provider = new JavaServiceProvider<>(serviceClass);
                bind(serviceClass).toProvider(provider);
            }
        };
    }
}
