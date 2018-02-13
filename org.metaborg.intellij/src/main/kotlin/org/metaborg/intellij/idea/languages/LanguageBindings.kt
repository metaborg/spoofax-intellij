package org.metaborg.intellij.idea.languages

import com.virtlink.editorservices.intellij.psi.AesiAstBuilder
import com.virtlink.editorservices.intellij.psi.AesiElementTypeManager
import com.virtlink.editorservices.intellij.psi.AesiTokenTypeManager
import org.metaborg.intellij.idea.extensions.InstanceLanguageExtensionPoint
import org.metaborg.intellij.idea.extensions.InstanceSyntaxHighlighterFactoryExtensionPoint
import org.metaborg.intellij.idea.files.SpoofaxFileType
import org.metaborg.intellij.idea.filetypes.MetaborgLanguageFileType
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenTypeManager


/**
 * IntelliJ IDEA objects bound to a language.
 *
 * The fields in this class contain language-specific extension points
 * that are registered when the language is activated, and unregistered
 * when the language is deactivated.
 *
 * @param tokenTypeManager The associated token type manager.
 * @param fileType The language file type.
 * @param parserDefinitionExtension The parser definition extension point value.
 * @param syntaxHighlighterFactoryExtension THe syntax highlighter factory extension.
 * @param elementTypeManager The element type manager.
 * @param tokenTypeManager2 The token type manager.
 */
internal data class LanguageBindings
(
        val tokenTypeManager: SpoofaxTokenTypeManager,
        val fileType: SpoofaxFileType,
        val parserDefinitionExtension: InstanceLanguageExtensionPoint<*>,
        val syntaxHighlighterFactoryExtension: InstanceSyntaxHighlighterFactoryExtensionPoint,
        val externalAnnotatorExtension: InstanceLanguageExtensionPoint<*>,
        val elementTypeManager: AesiElementTypeManager,
        val tokenTypeManager2: AesiTokenTypeManager,
        val astBuilder: AesiAstBuilder
)
