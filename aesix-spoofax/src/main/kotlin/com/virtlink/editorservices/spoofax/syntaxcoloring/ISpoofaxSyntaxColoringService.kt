package com.virtlink.editorservices.spoofax.syntaxcoloring

import com.virtlink.editorservices.ICancellationToken
import com.virtlink.editorservices.Span
import com.virtlink.editorservices.spoofax.ILanguage
import com.virtlink.editorservices.syntaxcoloring.ISyntaxColoringInfo
import com.virtlink.editorservices.syntaxcoloring.ISyntaxColoringService
import java.net.URI

interface ISpoofaxSyntaxColoringService: ISyntaxColoringService {

    /**
     * Colors (part of) a document.
     *
     * @param document The URI of the document to color.
     * @param language The language of the document; or null when it could not be determined.
     * @param span The area of the document to color.
     * @param cancellationToken The cancellation token.
     * @return A list of tokens.
     */
    fun getSyntaxColoringInfo(
            document: URI,
            language: ILanguage?,
            span: Span,
            cancellationToken: ICancellationToken)
            : ISyntaxColoringInfo?

    override fun getSyntaxColoringInfo(
            document: URI,
            span: Span,
            cancellationToken: ICancellationToken)
            : ISyntaxColoringInfo? {
        return getSyntaxColoringInfo(document, null, span, cancellationToken)
    }

}