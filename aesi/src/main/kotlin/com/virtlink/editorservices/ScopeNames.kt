package com.virtlink.editorservices

/**
 * A collection of scope names.
 */
class ScopeNames constructor(
        vararg val scopes: String) {

    /**
     * Determines whether the scopes list contains the specified scope name prefix.
     *
     * @param scopePrefix The scope name prefix to look for.
     * @return True when the scope was found; otherwise, false.
     */
    operator fun contains(scopePrefix: String): Boolean {
        return scopes.any { it.startsWith(scopePrefix, true) }
    }

    override fun toString(): String {
        return this.scopes.joinToString(", ", "[", "]")
    }

}