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
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
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
    public JavaServiceProvider(final Class<T> service) {
        this.service = service;
    }

    @Override
    public T get() {
        final ServiceLoader<T> loader = ServiceLoader.load(this.service);
        final Iterator<T> iterator = loader.iterator();

        if (!iterator.hasNext()) {
            throw LoggerUtils.exception(this.logger, ProvisionException.class,
                    "No implementations are registered for the class {}.",
                    this.service
            );
        }

        final T obj = iterator.next();

        if (iterator.hasNext()) {
            throw LoggerUtils.exception(this.logger, ProvisionException.class,
                    "Multiple implementations are registered for the class {}.",
                    this.service
            );
        }

        return obj;
    }
}
