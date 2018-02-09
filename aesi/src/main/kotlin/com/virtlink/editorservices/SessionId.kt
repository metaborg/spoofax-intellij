package com.virtlink.editorservices

import java.io.Serializable
import java.util.*

/**
 * A session ID.
 *
 * This uniquely identifies an editing session.
 */
@Suppress("DataClassPrivateConstructor")
data class SessionId private constructor(private val uuid: UUID): Serializable {
    constructor(): this(UUID.randomUUID())

    override fun toString(): String
            = uuid.toString()
}