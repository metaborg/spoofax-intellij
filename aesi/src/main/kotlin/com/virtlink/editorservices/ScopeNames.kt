package com.virtlink.editorservices

/**
 * A set of scope names.
 */
class ScopeNames constructor(
        val scopes: List<ScopeName>): Iterable<ScopeName> {

    constructor(vararg scopeNames: String)
        : this(scopeNames.map { ScopeName(it) })

    operator fun get(scopePrefix: String): ScopeName? {
        return this.scopes.firstOrNull { it.contains(scopePrefix) }
    }

    /**
     * Determines whether the scopes list contains any scope name with the specified scope name prefix.
     *
     * @param scopePrefix The scope name prefix to look for.
     * @return True when the scope name was found; otherwise, false.
     */
    operator fun contains(scopePrefix: String): Boolean {
        return this[scopePrefix] != null
    }

    override fun iterator(): Iterator<ScopeName> {
        return this.scopes.iterator()
    }

    override fun toString(): String {
        return this.scopes.joinToString(", ", "[", "]")
    }

}
