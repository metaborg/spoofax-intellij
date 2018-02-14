package com.virtlink.vfs

/**
 * Specifies the kind of path component.
 */
enum class FilePathComponentKind {
    /** The current folder (i.e. `./`). */
    Current,
    /** The parent folder (i.e. `../`). */
    Parent,
    /** A file system's root (i.e. `/`). */
    Root,
    /** A folder. */
    Folder,
    /** A file. */
    File
}