package com.virtlink.editorservices.syntaxcoloring

import com.google.inject.Inject
import com.virtlink.editorservices.ICancellationToken
import com.virtlink.editorservices.ScopeNames
import com.virtlink.editorservices.Span
import java.net.URI
import com.virtlink.editorservices.resources.IResourceManager

/**
 * Null implementation for when there is no actual implementation available.
 */
class NullSyntaxColoringService @Inject constructor()
    : ISyntaxColoringService {

    override fun configure(configuration: ISyntaxColoringConfiguration) {
        // Nothing to do.
    }

    override fun getSyntaxColoringInfo(document: URI, span: Span, cancellationToken: ICancellationToken): ISyntaxColoringInfo? {
        // Not supported.
        return null
    }
}