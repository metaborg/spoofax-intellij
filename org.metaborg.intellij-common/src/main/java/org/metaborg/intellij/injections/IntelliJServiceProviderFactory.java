/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.injections;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * Factory for IntelliJ service providers.
 *
 * This provider can't be used for project-level or module-level services.
 *
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
public final class IntelliJServiceProviderFactory {

    /**
     * Creates a provider for an IntelliJ service.
     *
     * @param serviceClass The service class.
     * @return A {@link Module} that binds the provider.
     */
    public <T> Module provide(final Class<T> serviceClass) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                final Provider<T> provider = new IntelliJServiceProvider<>(serviceClass);
                bind(serviceClass).toProvider(provider);
            }
        };
    }
}
