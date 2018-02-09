package com.virtlink.editorservices

/**
 * Cancellation token that never cancels.
 *
 * To be used when the adapter can't cancel the editor service.
 */
object NullCancellationToken : ICancellationToken {

    override val isCancelled: Boolean
        get() = false

}