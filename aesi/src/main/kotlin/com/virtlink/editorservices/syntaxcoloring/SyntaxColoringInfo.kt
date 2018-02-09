package com.virtlink.editorservices.syntaxcoloring

data class SyntaxColoringInfo(
        override val tokens: List<IToken>)
    : ISyntaxColoringInfo