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

package org.metaborg.intellij.jps;

import com.google.inject.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import java.util.*;

/**
 * Provides an instance from the Java {@link ServiceLoader}.
 */
/* package private */ final class JavaServiceProvider<T> implements Provider<T> {

    @InjectLogger
    private ILogger logger;
    private final Class<T> service;

    /**
     * Initializes a new instance of the {@link JavaServiceProvider} class.
     *
     * @param service The class of the service to load.
     */
    /* package private */ JavaServiceProvider(final Class<T> service) {
        this.service = service;
    }

    @Override
    public T get() {
        final ServiceLoader<T> loader = ServiceLoader.load(this.service);
        final Iterator<T> iterator = loader.iterator();

        if (!iterator.hasNext())
            throw new ProvisionException(this.logger.format(
                    "No implementations are registered for the class {}.",
                    this.service
            ));

        final T obj = iterator.next();

        if (iterator.hasNext())
            throw new ProvisionException(this.logger.format(
                    "Multiple implementations are registered for the class {}.",
                    this.service
            ));

        return obj;
    }
}
