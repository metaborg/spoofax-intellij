package com.virtlink.editorservices.content

import com.virtlink.editorservices.Offset
import com.virtlink.editorservices.Position
import com.virtlink.editorservices.resources.IAesiContent
import com.virtlink.editorservices.resources.IContent
import com.virtlink.editorservices.resources.TextChange
import java.io.LineNumberReader

/**
 * Empty content.
 */
class EmptyContent : IAesiContent {
    override val lastModificationStamp: Long
        get() = 0
    override val text: String
        get() = ""
    override val length: Long
        get() = 0
    override val lineCount: Int
        get() = 1

    override fun getOffset(position: Position): Offset?
            = if (position == Position(0, 0)) 0 else null

    override fun getPosition(offset: Offset): Position?
            = if (offset == 0) Position(0, 0) else null

    override fun withChanges(changes: List<TextChange>, newStamp: Long): IContent
            = StringContent.empty.withChanges(changes, newStamp)
}