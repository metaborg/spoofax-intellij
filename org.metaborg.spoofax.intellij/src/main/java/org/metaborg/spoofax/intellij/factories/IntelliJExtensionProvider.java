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

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.intellij.openapi.extensions.Extensions;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.StringFormatter;

/**
 * Provides an instance from the IntelliJ extensions.
 */
/* package private */ final class IntelliJExtensionProvider<T> implements Provider<T> {

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
        throw new ProvisionException(StringFormatter.format(
                "No extensions are registered for the class {} in extension point {}.",
                this.extensionClass,
                this.extensionPointName
        ));
    }
}
