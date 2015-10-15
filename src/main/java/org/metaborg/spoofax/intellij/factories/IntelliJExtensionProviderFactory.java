package org.metaborg.spoofax.intellij.factories;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for IntelliJ extension providers.
 *
 * Usage:
 * <pre>{@code
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
 * }</pre>
 */
public final class IntelliJExtensionProviderFactory {

    /**
     * {@inheritDoc}
     */
    public <T> Module provide(@NotNull final Class<T> extensionClass, @NotNull final String extensionPointName) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                Provider<T> provider = new IntelliJExtensionProvider<T>(extensionClass, extensionPointName);
                bind(extensionClass).toProvider(provider);
            }
        };
    }
}
