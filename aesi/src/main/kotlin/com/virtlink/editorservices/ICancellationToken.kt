package com.virtlink.editorservices

/**
 * Object which can indicate whether the operation has been cancelled.
 */
interface ICancellationToken {

    /**
     * Gets whether the operation has been cancelled.
     */
    val isCancelled: Boolean

}