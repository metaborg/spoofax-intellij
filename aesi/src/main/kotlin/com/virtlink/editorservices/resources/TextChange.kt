package com.virtlink.editorservices.resources

import com.virtlink.editorservices.Span

/**
 * Describes a change to the text of a document.
 *
 * @property span The region of the document to change. This may be an empty span when text is only inserted.
 * @property newText The text to replace the span with. This may be an empty string when text is only removed.
 */
data class TextChange(
        val span: Span,
        val newText: String
)