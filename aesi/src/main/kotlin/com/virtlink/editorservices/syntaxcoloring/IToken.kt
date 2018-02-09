package com.virtlink.editorservices.syntaxcoloring

import com.virtlink.editorservices.ScopeNames
import com.virtlink.editorservices.Span
import java.io.Serializable

/**
 * A coloring token.
 */
interface IToken : Serializable {

    /**
     * Gets the span in the document that is being colored.
     */
    val location: Span

    /**
     * Gets the scope names assigned to the token.
     *
     * The names are used to find an appropriate color for the token.
     */
    val scopes: ScopeNames

}