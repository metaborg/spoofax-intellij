package com.virtlink.editorservices.intellij.psi

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import com.intellij.lang.Language
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.tree.TokenSet

/**
 * Tracks element types.
 *
 * For token types, see [AesiTokenTypeManager].
 */
class AesiElementTypeManager
@Inject constructor(@Assisted language: Language)  {

    /**
     * Factory.
     */
    interface IFactory {

        /**
         * Creates the element type manager.
         *
         * @param language The language.
         */
        fun create(language: Language): AesiElementTypeManager
    }

    val rootElementType = AesiRootElementType(language)
    val defaultElementType = AesiElementType(language)
    private val namedElementType = AesiNamedElementType(language)

    private val scopes = listOf(
            scopeType("entity.name", { namedElementType })
    )

    fun getElementType(type: AesiTokenType): AesiElementType {
        return this.scopes
                .filter { (prefix, _) -> prefix in type.scopes }
                .map { (_, type) -> type() }
                .firstOrNull() ?: defaultElementType
    }

    private fun scopeType(prefix: String, type: () -> AesiElementType): Pair<String, () -> AesiElementType> {
        return Pair(prefix, type)
    }
}