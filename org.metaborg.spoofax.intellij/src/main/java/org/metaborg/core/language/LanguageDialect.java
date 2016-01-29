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

package org.metaborg.core.language;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;

/**
 * A language implementation/dialect tuple.
 */
public final class LanguageDialect {

    private final ILanguageImpl baseLanguage;
    @Nullable
    private final ILanguageImpl dialectLanguage;

    /**
     * Initializes a new instance of the {@link LanguageDialect} class.
     *
     * @param baseLanguage    The base language implementation.
     * @param dialectLanguage The dialect language implementation; or <code>null</code>.
     */
    public LanguageDialect(final ILanguageImpl baseLanguage, @Nullable final ILanguageImpl dialectLanguage) {
        Preconditions.checkNotNull(baseLanguage);

        this.baseLanguage = baseLanguage;
        this.dialectLanguage = dialectLanguage;
    }

    /**
     * Gets the base language implementation.
     *
     * @return The base language implementation.
     */
    public ILanguageImpl baseLanguage() {
        return this.baseLanguage;
    }

    /**
     * Gets the dialect language implementation.
     *
     * @return The dialect language implementation;
     * or <code>null</code> when there is no dialect.
     */
    @Nullable
    public ILanguageImpl dialectLanguage() {
        return this.dialectLanguage;
    }

    /**
     * Gets the dialect language implementation if available;
     * otherwise, the base language implementation.
     *
     * @return The dialect or base language implementation.
     */
    public ILanguageImpl dialectOrBaseLanguage() {
        return this.dialectLanguage != null ? this.dialectLanguage : this.baseLanguage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return dialectOrBaseLanguage().toString();
    }
}
