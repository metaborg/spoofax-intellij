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

import com.google.common.base.Preconditions;
import org.slf4j.helpers.MessageFormatter;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

/**
 * A setting key.
 * <p>
 * This describes the name, type and other attributes of a setting.
 * <p>
 * Be sure to store and reuse the <em>exact same key object</em>,
 * as different instances are not equal (even if they have the same name and type).
 * <p>
 * Examples:
 * <pre>
 * static final SettingKey NAME_KEY = SettingKey.create("name", String.class);
 * static final SettingKey ID_LIST_KEY = SettingKey.create("ids", new TypeReference<List<LanguageIdentifier>>() {}, SettingStrategies.Union);
 * </pre>
 *
 * @param <T> The type of value.
 */
public final class SettingKey<T> implements ISettingKey<T> {

    private final String name;
    private final Type type;
    private final ISettingInheritanceStrategy<T> inheritanceStrategy;

    /**
     * Initializes a new instance of the {@link SettingKey} class.
     *
     * @param name The name of the setting.
     * @param type The type of the setting.
     */
    public SettingKey(final String name, final Class<T> type) {
        this(name, type, null);
    }

    /**
     * Initializes a new instance of the {@link SettingKey} class.
     *
     * @param name                The name of the setting.
     * @param type                The type of the setting.
     * @param inheritanceStrategy The inheritance strategy of the setting;
     *                            or <code>null</code> to use the default: override.
     */
    public SettingKey(
            final String name,
            final Class<T> type,
            @Nullable final ISettingInheritanceStrategy<T> inheritanceStrategy) {
        this(name, (Type) type, inheritanceStrategy);
    }

    /**
     * Initializes a new instance of the {@link SettingKey} class.
     *
     * @param name                The name of the setting.
     * @param type                The type of the setting.
     * @param inheritanceStrategy The inheritance strategy of the setting.
     */
    private SettingKey(
            final String name,
            final Type type,
            @Nullable final ISettingInheritanceStrategy<T> inheritanceStrategy) {
        Preconditions.checkNotNull(name);
        Preconditions.checkNotNull(type);

        this.name = name;
        this.type = type;
        // TODO: Optimize this with singleton:
        this.inheritanceStrategy = inheritanceStrategy != null ? inheritanceStrategy : new SettingOverrideStrategy<>();
    }

    /**
     * Initializes a new instance of the {@link SettingKey} class.
     *
     * @param name The name of the setting.
     * @param type The type of the setting.
     */
    public SettingKey(final String name, final TypeReference<T> type) {
        this(name, type, null);
    }

    /**
     * Initializes a new instance of the {@link SettingKey} class.
     *
     * @param name                The name of the setting.
     * @param type                The type of the setting.
     * @param inheritanceStrategy The inheritance strategy of the setting;
     *                            or <code>null</code> to use the default: override.
     */
    public SettingKey(
            final String name,
            final TypeReference<T> type,
            @Nullable final ISettingInheritanceStrategy<T> inheritanceStrategy) {
        this(name, type.type(), inheritanceStrategy);
    }

    /**
     * {@inheritDoc}
     */
    public String name() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    public Type type() {
        return this.type;
    }

    /**
     * {@inheritDoc}
     */
    public ISettingInheritanceStrategy<T> inheritanceStrategy() { return this.inheritanceStrategy;}

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        // NOTE: The hash code never changes for this object.
        return System.identityHashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        // NOTE: Reference equality.
        return this == obj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return MessageFormatter.format("{}<{}>", this.name, this.type.getTypeName()).getMessage();
    }
}
