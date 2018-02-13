package com.virtlink.editorservices.intellij.psi

import com.intellij.lang.Language
import com.intellij.psi.tree.IElementType
import com.virtlink.editorservices.ScopeNames

/**
 * Element type for tokens.
 */
class AesiTokenType(
        val scopes: ScopeNames,
        language: Language)
    : IElementType(scopes.toString(), language)

//
///**
// * Element type for tokens.
// */
//class AesiTokenType(val scope: String, language: Language)
//    : IElementType(scope, language)