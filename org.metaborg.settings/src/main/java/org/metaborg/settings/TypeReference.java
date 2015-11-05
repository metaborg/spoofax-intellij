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

package org.metaborg.settings;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * A type reference, used to represent a complex generic type.SettingKey
 * <p>
 * Example:
 * <pre>
 * TypeReference myRef = new TypeReference&lt;List&lt;String&gt;&gt;() {};
 * </pre>
 * <p>
 * {@see http://gafter.blogspot.nl/2006/12/super-type-tokens.html}
 */
public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {

    private final Type type;

    /**
     * Initializes a new instance of the {@link TypeReference} class.
     */
    protected TypeReference() {
        Type type = getClass().getGenericSuperclass();
        assert !(type instanceof Class<?>);
        this.type = ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    /**
     * Gets the type wrapped by this type reference.
     *
     * @return The {@link Type}.
     */
    public Type type() {
        return this.type;
    }

    /*
     * This implementation of Comparable<T> is only to disallow leaving out the type.
     * The implementation itself does not matter.
     */
    @Override
    public int compareTo(TypeReference<T> other) { return 0; }
}
