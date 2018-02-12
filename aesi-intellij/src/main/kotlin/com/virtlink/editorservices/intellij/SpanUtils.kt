package com.virtlink.editorservices.intellij

import com.intellij.openapi.util.TextRange
import com.virtlink.editorservices.Span

/**
 * Converts a [Span] to a [TextRange].
 */
fun Span.toTextRange(): TextRange = TextRange(this.startOffset, this.endOffset)

/**
 * Converts a [TextRange] to a [Span].
 */
fun TextRange.toSpan(): Span = Span(this.startOffset, this.endOffset)