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

import com.google.common.collect.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.*;

import java.util.*;

// TODO: Move these to Metaborg core?

/**
 * Utility functions for working with languages.
 */
public final class LanguageUtils2 {

    private LanguageUtils2() {}

    /**
     * Returns whether the specified language is an actual language with syntax,
     * or just a support library packaged as a language.
     *
     * @param language The language to check.
     * @return <code>true</code> when the language is an actual language;
     * otherwise, <code>false</code> when the language is not an actual language.
     */
    public static boolean isRealLanguage(final ILanguage language) {
        // For now, we test this by checking the file extensions.
        // If there are none, then it's not a language.
        return !getExtensions(language).isEmpty();
    }

    /**
     * Gets the set of language identifiers for the given language implementations.
     *
     * @param languageImpls The language implementations.
     * @return The language identifiers.
     */
    public static Set<LanguageIdentifier> getIdentifiersOfLanguageImplementations(final Iterable<ILanguageImpl> languageImpls) {
        final Set<LanguageIdentifier> ids = new HashSet<>();
        for (final ILanguageImpl languageImpl : languageImpls) {
            ids.add(languageImpl.id());
        }
        return ids;
    }

    /**
     * Gets the set of languages to which all given language components belong.
     *
     * @param components The components.
     * @return The languages to which they belong.
     */
    public static Set<ILanguage> getLanguagesOfComponents(final Iterable<ILanguageComponent> components) {
        return getLanguagesOfImplementations(getLanguageImplsOfComponents(components));
    }

    /**
     * Gets the set of languages to which all given language implementations belong.
     *
     * @param implementations The language implementations.
     * @return The languages to which they belong.
     */
    public static Set<ILanguage> getLanguagesOfImplementations(final Iterable<ILanguageImpl> implementations) {
        final Set<ILanguage> languages = new HashSet<>();
        for (final ILanguageImpl implementation : implementations) {
            languages.add(implementation.belongsTo());
        }
        return languages;
    }

    /**
     * Gets the set of language implementations to which all given language components belong.
     *
     * @param components The language components.
     * @return The language implementations to which they belong.
     */
    public static Set<ILanguageImpl> getLanguageImplsOfComponents(final Iterable<ILanguageComponent> components) {
        final Set<ILanguageImpl> languageImpls = new HashSet<>();
        for (final ILanguageComponent component : components) {
            languageImpls.addAll(Lists.newArrayList(component.contributesTo()));
        }
        return languageImpls;
    }

    /**
     * Given an artifact file, returns the URI of the artifact file's root.
     *
     * @param artifact The artifact file.
     * @return The URI of the artifact file's root.
     */
    public static String getArtifactUri(final FileObject artifact) {
        final String zipUri;
        try {
            zipUri = "zip://" + artifact.getURL().getPath();
        } catch (final FileSystemException e) {
            throw new UnhandledException(e);
        }
        return zipUri;
    }

    /**
     * Returns a set with all supported file extensions for a particular language.
     *
     * @param language The language.
     * @return A set of extensions, which may be empty.
     */
    public static Set<String> getExtensions(final ILanguage language) {
        // FIXME: The extensions for a language should be stored in the ILanguage object,
        // not the ILanguageImpl objects.
        // For now, we take all known language implementations and use all those extensions.
        // The downside is that: if two implementations for a language define different extensions,
        // then both are used everywhere. Additionally, if the extension for an implementation
        // changes, then any users of the result of this method are not notified of the change.
        final Set<String> extensions = Sets.newHashSet();
        for (final ILanguageImpl impl : language.impls()) {
            for (final ResourceExtensionFacet facet : impl.facets(ResourceExtensionFacet.class)) {
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
    public static String getDefaultExtension(final ILanguage language) {
        // FIXME: The first of a set is non-deterministic! Also, shouldn't every language
        // have a settable default extension that is used when files of that language are created?
        final Set<String> extensions = getExtensions(language);
        if (extensions.isEmpty())
            throw new RuntimeException("No extensions registered for language.");
        return extensions.iterator().next();
    }
}
