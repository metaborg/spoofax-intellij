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
import com.intellij.openapi.components.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.util.log.*;

import javax.annotation.*;

/**
 * Provides an instance from the IntelliJ service manager.
 */
/* package private */ final class IntelliJServiceProvider<T> implements Provider<T> {

    @InjectLogger
    private ILogger logger;
    private final Class<T> service;

    /**
     * Initializes a new instance of the {@link IntelliJServiceProvider} class.
     *
     * @param service The class of the service to load.
     */
    public IntelliJServiceProvider(final Class<T> service) {
        this.service = service;
    }

    @Override
    public T get() {
        @Nullable final T service = ServiceManager.getService(this.service);
        if (service == null) {
            throw LoggerUtils.exception(this.logger, ProvisionException.class,
                    "No implementations are registered for the class {}.",
                    this.service
            );
        }
        return service;
    }
}
