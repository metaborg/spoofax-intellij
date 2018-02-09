package com.virtlink.editorservices.syntaxcoloring

import com.virtlink.editorservices.ICancellationToken
import com.virtlink.editorservices.Span
import java.net.URI

/**
 * Colors (part of) a document.
 */
interface ISyntaxColoringService {

    /**
     * Configures the service.
     */
    fun configure(configuration: ISyntaxColoringConfiguration)

    /**
     * Colors (part of) a document.
     *
     * @param document The URI of the document to color.
     * @param span The area of the document to color.
     * @param cancellationToken The cancellation token.
     * @return A list of tokens.
     */
    fun getSyntaxColoringInfo(
            document: URI,
            span: Span,
            cancellationToken: ICancellationToken)
            : ISyntaxColoringInfo?
}