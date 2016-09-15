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

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.intellij.openapi.application.ApplicationManager;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.util.log.*;

import javax.annotation.Nullable;

/**
 * Provides an instance from the IntelliJ component manager.
 */
/* package private */ final class IntelliJComponentProvider<T> implements Provider<T> {

    @InjectLogger
    private ILogger logger;
    private final Class<T> service;

    /**
     * Initializes a new instance of the {@link IntelliJComponentProvider} class.
     *
     * @param service The class of the component to load.
     */
    public IntelliJComponentProvider(final Class<T> service) {
        this.service = service;
    }

    @Override
    public T get() {
        @Nullable final T component = ApplicationManager.getApplication().getComponent(this.service);
        if (component == null) {
            throw LoggerUtils2.exception(this.logger, ProvisionException.class,
                    "No implementations are registered for the class {}.",
                    this.service
            );
        }
        return component;
    }
}
