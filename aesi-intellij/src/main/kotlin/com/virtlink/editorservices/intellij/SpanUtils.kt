package com.virtlink.editorservices.intellij

import com.intellij.openapi.util.TextRange
import com.virtlink.editorservices.Span

/**
 * Converts a [Span] to a [TextRange].
 */
fun Span.toTextRange(): TextRange = TextRange(this.startOffset.toInt(), this.endOffset.toInt())

/**
 * Converts a [TextRange] to a [Span].
 */
fun TextRange.toSpan(): Span = Span(this.startOffset.toLong(), this.endOffset.toLong())