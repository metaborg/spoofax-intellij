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

package org.metaborg.intellij.logging;

import com.google.common.base.Preconditions;
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
        Preconditions.checkNotNull(typeLiteral);
        Preconditions.checkNotNull(typeEncounter);

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

