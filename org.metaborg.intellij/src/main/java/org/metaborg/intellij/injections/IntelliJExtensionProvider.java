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

import com.google.inject.*;
import com.intellij.openapi.extensions.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.util.log.*;

/**
 * Provides an instance from the IntelliJ extensions.
 */
/* package private */ final class IntelliJExtensionProvider<T> implements Provider<T> {

    @InjectLogger
    private ILogger logger;
    private final Class<T> extensionClass;
    private final String extensionPointName;

    /**
     * Initializes a new instance of the {@link IntelliJExtensionProvider} class.
     *
     * @param extensionClass     The class of the extension to load.
     * @param extensionPointName The extension point name.
     */
    /* package private */ IntelliJExtensionProvider(
            final Class<T> extensionClass,
            final String extensionPointName) {
        this.extensionClass = extensionClass;
        this.extensionPointName = extensionPointName;
    }

    @Override
    public T get() {
        final Object[] candidates = Extensions.getExtensions(this.extensionPointName);

        for (final Object candidate : candidates) {
            if (candidate.getClass().equals(this.extensionClass)) {
                return (T)candidate;
            }
        }
        throw LoggerUtils.exception(this.logger, ProvisionException.class,
                "No extensions are registered for the class {} in extension point {}.",
                this.extensionClass,
                this.extensionPointName
        );
    }
}
