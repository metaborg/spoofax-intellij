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

package org.metaborg.intellij.idea.languages;

import com.google.common.cache.*;
import com.google.common.collect.*;
import com.google.inject.*;
import org.apache.commons.lang.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Manages tags on {@link ILanguage} objects.
 *
 * This implementation is thread-safe.
 */
@Deprecated
public final class LanguageTagManager {

    private final LoadingCache<ILanguage, ConcurrentMap<ILanguageTag<?>, Object>> languageTags;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link LanguageTagManager} class.
     */
    @Inject
    public LanguageTagManager() {
        final CacheLoader<ILanguage, ConcurrentMap<ILanguageTag<?>, Object>> loader = new CacheLoader<ILanguage, ConcurrentMap<ILanguageTag<?>, Object>>() {

            @Override
            public ConcurrentMap<ILanguageTag<?>, Object> load(final ILanguage iLanguage)
                    throws Exception {
                return new ConcurrentHashMap<>();
            }
        };
        this.languageTags = CacheBuilder.newBuilder().weakKeys().build(loader);
    }

    /**
     * Tags the specified language with the specified tag and value.
     *
     * If the language and tag are already present, the value is overwritten.
     *
     * @param language The language.
     * @param tag The tag.
     * @param value The value.
     * @param <T> The type of value.
     */
    public <T> void tag(final ILanguage language, final ILanguageTag<T> tag, final T value) {
        final ConcurrentMap<ILanguageTag<?>, Object> tags = this.languageTags.getUnchecked(language);
        tags.put(tag, value);
    }

    /**
     * Untags the specified tag from the specified language.
     *
     * If the language and tag combination is not present, nothing happens.
     *
     * @param language The language.
     * @param tag The tag.
     * @param <T> The type of value.
     */
    public <T> void untag(final ILanguage language, final ILanguageTag<T> tag) {
        @Nullable ConcurrentMap<ILanguageTag<?>, Object> tags = this.languageTags.getIfPresent(language);
        if (tags == null)
            return;
        tags.remove(tag);
    }

    /**
     * Gets the value associated with the specified language and tag.
     *
     * @param language The language.
     * @param tag The tag.
     * @param <T> The type of value.
     * @return The value of the tag; or <code>null</code> when not found.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T> T getTagValue(final ILanguage language, final ILanguageTag<T> tag) {
        @Nullable ConcurrentMap<ILanguageTag<?>, Object> tags = this.languageTags.getIfPresent(language);
        if (tags == null)
            return null;
        return (T)tags.get(tag);
    }

    /**
     * Gets all tags associated with the specified language.
     *
     * @param language The language.
     * @return A set of tags.
     */
    public Set<ILanguageTag<?>> getAllTags(final ILanguage language) {
        @Nullable ConcurrentMap<ILanguageTag<?>, Object> tags = this.languageTags.getIfPresent(language);
        if (tags == null)
            return Collections.emptySet();
        return Sets.newHashSet(tags.keySet());
    }

}
