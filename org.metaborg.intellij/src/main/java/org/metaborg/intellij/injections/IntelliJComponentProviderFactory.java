/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.injections;

import com.google.inject.*;

/**
 * Factory for IntelliJ component providers.
 *
 * This provider can't be used for project-level or module-level components.
 *
 * <p>
 * Usage:
 * <pre>
 * class MyModule extends AbstractModule {
 *
 *     &#064;Override
 *     protected void configure() {
 *         // ...
 *         install(new IntelliJComponentProviderFactory().provide(IMyInterface.class));
 *
 *     }
 *
 * }
 * </pre>
 */
public final class IntelliJComponentProviderFactory {

    /**
     * Creates a provider for an IntelliJ component.
     *
     * @param serviceClass The component class.
     * @return A {@link Module} that binds the provider.
     */
    public <T> Module provide(final Class<T> serviceClass) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                final Provider<T> provider = new IntelliJComponentProvider<>(serviceClass);
                bind(serviceClass).toProvider(provider);
            }
        };
    }
}
