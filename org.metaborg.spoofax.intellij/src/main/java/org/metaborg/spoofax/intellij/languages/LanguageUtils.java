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

package org.metaborg.spoofax.intellij.languages;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ResourceExtensionFacet;

import javax.annotation.Nullable;
import java.util.Set;

// TODO: Move these to Metaborg core?

/**
 * Utility functions for working with languages.
 */
public final class LanguageUtils {

    private LanguageUtils() {}

    /**
     * Returns whether the specified language is an actual language with syntax,
     * or just a support library packaged as a language.
     *
     * @param language The language to check.
     * @return <code>true</code> when the language is an actual language;
     * otherwise, <code>false</code> when the language is not an actual language.
     */
    public final static boolean isRealLanguage(@NotNull final ILanguage language) {
        // For now, we test this by checking the file extensions.
        // If there are none, then it's not a language.
        return getExtensions(language).size() != 0;
    }

    /**
     * Returns a set with all supported file extensions for a particular language.
     *
     * @param language The language.
     * @return A set of extensions, which may be empty.
     */
    @NotNull
    public final static Set<String> getExtensions(@NotNull final ILanguage language) {
        // FIXME: The extensions for a language should be stored in the ILanguage object,
        // not the ILanguageImpl objects.
        // For now, we take all known language implementations and use all those extensions.
        // The downside is that: if two implementations for a language define different extensions,
        // then both are used everywhere. Additionally, if the extension for an implementation
        // changes, then any users of the result of this method are not notified of the change.
        final Set<String> extensions = Sets.newHashSet();
        for (ILanguageImpl impl : language.impls()) {
            for (ResourceExtensionFacet facet : impl.facets(ResourceExtensionFacet.class)) {
                Iterables.addAll(extensions, facet.extensions());
            }
        }
        return extensions;
    }

    /**
     * Returns the default file extension for a particular language.
     *
     * @param language The language.
     * @return The default file extension; or <code>null</code>.
     */
    @NotNull
    public final static String getDefaultExtension(@NotNull final ILanguage language) {
        // FIXME: The first of a set is non-deterministic! Also, shouldn't every language
        // have a settable default extension that is used when files of that language are created?
        final Set<String> extensions = getExtensions(language);
        if (extensions.isEmpty())
            throw new RuntimeException("No extensions registered for language.");
        return extensions.iterator().next();
    }
}
