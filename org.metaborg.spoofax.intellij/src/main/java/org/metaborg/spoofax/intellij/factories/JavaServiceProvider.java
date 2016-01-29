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
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.StringFormatter;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Provides an instance from the Java {@link java.util.ServiceLoader}.
 */
/* package private */ final class JavaServiceProvider<T> implements Provider<T> {

    @NotNull
    private final Class<T> service;

    /**
     * Initializes a new instance of the {@link JavaServiceProvider} class.
     *
     * @param service The class of the service to load.
     */
    /* package private */ JavaServiceProvider(@NotNull final Class<T> service) {
        this.service = service;
    }

    @Override
    @NotNull
    public T get() {
        final ServiceLoader<T> loader = ServiceLoader.load(this.service);
        final Iterator<T> iterator = loader.iterator();

        if (!iterator.hasNext())
            throw new ProvisionException(StringFormatter.format(
                    "No implementations are registered for the class {}.",
                    this.service
            ));

        final T obj = iterator.next();

        if (iterator.hasNext())
            throw new ProvisionException(StringFormatter.format(
                    "Multiple implementations are registered for the class {}.",
                    this.service
            ));

        return obj;
    }
}
