package com.virtlink.editorservices.resources

import com.virtlink.editorservices.Offset
import com.virtlink.editorservices.Position

interface IAesiContent: IContent {
    fun getOffset(position: Position): Offset?

    fun getPosition(offset: Offset): Position?
}