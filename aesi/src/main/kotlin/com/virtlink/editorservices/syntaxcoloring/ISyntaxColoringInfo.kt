package com.virtlink.editorservices.syntaxcoloring

/**
 * Returned by the [ISyntaxColoringService].
 */
interface ISyntaxColoringInfo {

    /**
     * Gets the tokens.
     */
    val tokens: List<IToken>

}