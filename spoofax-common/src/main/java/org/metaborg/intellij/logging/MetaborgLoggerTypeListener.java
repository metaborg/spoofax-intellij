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

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.metaborg.util.log.ILogger;

import java.lang.reflect.Field;

/**
 * Listens for injections in a type.
 */
public final class MetaborgLoggerTypeListener implements TypeListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public final <I> void hear(
            final TypeLiteral<I> typeLiteral,
            final TypeEncounter<I> typeEncounter) {
        if (typeLiteral == null) {
          throw new NullPointerException();
        }
        if (typeEncounter == null) {
          throw new NullPointerException();
        }

        // Go through the class and its ancestors to find fields like this:
        //     @InjectLogger private Logger logger;

        Class<?> clazz = typeLiteral.getRawType();
        while (clazz != null) {
            for (final Field field : clazz.getDeclaredFields()) {
                if (field.getType() == ILogger.class &&
                        field.isAnnotationPresent(InjectLogger.class)) {
                    typeEncounter.register(new MetaborgLoggerMembersInjector<>(field));
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}

