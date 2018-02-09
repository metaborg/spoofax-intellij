package com.virtlink.editorservices.intellij

import com.intellij.openapi.progress.ProgressIndicator
import com.virtlink.editorservices.ICancellationToken

fun ProgressIndicator.toCancellationToken(): ICancellationToken {
    return ProgressCancellationToken(this)
}

private class ProgressCancellationToken(val progress: ProgressIndicator) : ICancellationToken {

    override val isCancelled: Boolean
        get() = this.progress.isCanceled

}