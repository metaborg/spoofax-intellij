package org.metaborg.spoofax.intellij.factories;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for IntelliJ service providers.
 * <p>
 * Usage:
 * <pre>
 * class MyModule extends AbstractModule {
 *
 *     @Override
 *     protected void configure() {
 *         // ...
 *         install(new IntelliJServiceProviderFactory().provide(IMyInterface.class));
 *
 *     }
 *
 * }
 * </pre>
 */
public final class IntelliJServiceProviderFactory {

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
                Provider<T> provider = new IntelliJServiceProvider<T>(serviceClass);
                bind(serviceClass).toProvider(provider);
            }
        };
    }
}
