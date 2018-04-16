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

package org.metaborg.intellij.logging;

import com.google.common.base.Preconditions;
import com.google.inject.MembersInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Injects loggers.
 *
 * @param <T> The type of object.
 */
/* package private */ final class Slf4JLoggerMembersInjector<T> implements MembersInjector<T> {

    private final Field field;
    private final Logger logger;

    /**
     * Initializes a new instance of the {@link Slf4JLoggerMembersInjector} class.
     *
     * @param field The field to inject.
     */
    public Slf4JLoggerMembersInjector(final Field field) {
        Preconditions.checkNotNull(field);

        this.field = field;
        this.logger = createLogger(field.getDeclaringClass());
        this.field.setAccessible(true);
    }

    /**
     * Creates the logger.
     *
     * @param clazz The class.
     * @return The created logger instance.
     */
    private static Logger createLogger(final Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Injects the logger in the specified object.
     *
     * @param obj The object.
     */
    @Override
    public void injectMembers(final T obj) {
        Preconditions.checkNotNull(obj);

        try {
            this.field.set(obj, this.logger);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Unexpected exception: " + e);
        }
    }
}
