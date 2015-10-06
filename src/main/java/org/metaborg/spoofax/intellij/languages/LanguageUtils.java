package org.metaborg.spoofax.intellij.languages;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
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
     * Returns the default file extension for a particular language component.
     *
     * @param languageImpl The language implementation.
     * @return The default file extension; or <code>null</code>.
     */
    @Nullable
    public final static String getDefaultExtension(@NotNull final ILanguageImpl languageImpl) {
        // FIXME: The first of a set is non-deterministic! Also, shouldn't every language
        // have a settable default extension that is used when files of that language are created?
        final Set<String> extensions = getExtensions(languageImpl);
        if (extensions.isEmpty())
            return null;
        return extensions.iterator().next();
    }

    /**
     * Returns a set with all supported file extensions for a particular language component.
     *
     * @param languageImpl The language implementation.
     * @return A set of extensions, which may be empty.
     */
    @NotNull
    public final static Set<String> getExtensions(@NotNull final ILanguageImpl languageImpl) {
        final Set<String> extensions = Sets.newHashSet();
        for(ResourceExtensionFacet facet : languageImpl.facets(ResourceExtensionFacet.class)) {
            Iterables.addAll(extensions, facet.extensions());
        }
        return extensions;
    }
}
