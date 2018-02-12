package com.virtlink.editorservices.spoofax.syntaxcoloring

import com.virtlink.editorservices.ScopeNames
import com.virtlink.editorservices.Span
import com.virtlink.editorservices.syntaxcoloring.IToken

/**
 * A Spoofax token.
 *
 * @property location The token location.
 * @property scopes The token's scope names.
 */
class SpoofaxToken(
        override val location: Span,
        override val scopes: ScopeNames)
    : IToken