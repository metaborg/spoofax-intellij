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

package org.metaborg.intellij.idea.languages;

import com.google.common.base.*;
import org.metaborg.core.language.*;

import javax.annotation.*;

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
