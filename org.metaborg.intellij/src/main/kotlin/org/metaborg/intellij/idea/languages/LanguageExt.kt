package org.metaborg.intellij.idea.languages

import org.metaborg.core.language.ILanguage
import org.metaborg.core.language.ResourceExtensionFacet

/**
 * Gets the default file extension of the language;
 * or null when it has no default extension.
 */
val ILanguage.defaultExtension: String? get() {
    // TODO SPOOFAX: Every language should have a settable default extension that is used when files of that language are created
    // FIXME: The first of a set is non-deterministic!
    val extensions = this.extensions
    if (extensions.isEmpty())
        throw RuntimeException("No extensions registered for language.")
    return extensions.iterator().next()
}

/**
 * Gets a set with all supported file extensions for a particular language.
 *
 * @param language The language.
 * @return A set of extensions, which may be empty.
 */
val ILanguage.extensions: Set<String> get() {
    // TODO SPOOFAX: The extensions for a language should be stored in the ILanguage object, not the ILanguageImpl objects.

    // For now, we take all known language implementations and use all those extensions.
    // The downside is that: if two implementations for a language define different extensions,
    // then both are used everywhere. Additionally, if the extension for an implementation
    // changes, then any users of the result of this method are not notified of the change.

    return this.impls()
            .flatMap { it.facets(ResourceExtensionFacet::class.java) }
            .flatMap { it.extensions() }
            .toSet()
}

/**
 * Gets whether the specified language is an actual language with syntax,
 * or just a support library packaged as a language.
 */
val ILanguage.isReal: Boolean get() {
    // For now, we test this by checking the file extensions.
    // If there are none, then it's not a language.
    return this.extensions.isNotEmpty()
}