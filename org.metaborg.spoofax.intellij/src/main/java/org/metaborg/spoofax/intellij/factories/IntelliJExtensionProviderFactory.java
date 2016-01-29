/*
 * Copyright © 2015-2015
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.spoofax.intellij.factories;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for IntelliJ extension providers.
 * <p>
 * Usage:
 * <pre>
 * class MyModule extends AbstractModule {
 *
 *     &#064;Override
 *     protected void configure() {
 *         // ...
 *         install(new IntelliJServiceProviderFactory().provide(IMyInterface.class));
 *
 *     }
 *
 * }
 * </pre>
 */
public final class IntelliJExtensionProviderFactory {

    /**
     * Creates a provider for an extension point implementation.
     *
     * @param extensionClass     The extension class.
     * @param extensionPointName The extension point name.
     * @return A {@link Module} that binds the provider.
     */
    public <T> Module provide(final Class<T> extensionClass, final String extensionPointName) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                final Provider<T> provider = new IntelliJExtensionProvider<>(extensionClass, extensionPointName);
                bind(extensionClass).toProvider(provider);
            }
        };
    }
}
