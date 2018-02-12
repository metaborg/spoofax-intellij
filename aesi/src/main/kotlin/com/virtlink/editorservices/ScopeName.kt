package com.virtlink.editorservices

/**
 * A scope name.
 */
class ScopeName constructor(val name: String) {

    /**
     * Determines whether the scope name starts with the specified scope name prefix.
     *
     * @param scopePrefix The scope name prefix to look for.
     * @return True when the scope name starts with the prefix; otherwise, false.
     */
    operator fun contains(scopePrefix: String): Boolean {
        // The scope name, e.g. `a.b.c.d`,
        // must start with the specified prefix, e.g. `a.b`,
        // followed by either the end of the scope name,
        // or a dot `.` (thus `a.bc` does not match the prefix `a.b`).
        return this.name.startsWith(scopePrefix, true)
                && (this.name.length == scopePrefix.length || this.name[scopePrefix.length] == '.')
    }

    override fun toString(): String = this.name
}