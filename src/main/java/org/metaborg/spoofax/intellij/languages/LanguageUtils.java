package org.metaborg.spoofax.intellij.languages;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageComponent;
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
     * Returns the default file extension for a particular language.
     *
     * @param language The language.
     * @return The default file extension; or <code>null</code>.
     */
    @Nullable
    public final static String getDefaultExtension(@NotNull final ILanguage language) {
        // FIXME: The first of a set is non-deterministic! Also, shouldn't every language
        // have a settable default extension that is used when files of that language are created?
        final Set<String> extensions = getExtensions(language);
        if (extensions.isEmpty())
            return null;
        return extensions.iterator().next();
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
}
