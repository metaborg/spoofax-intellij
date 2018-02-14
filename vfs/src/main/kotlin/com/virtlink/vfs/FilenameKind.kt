package com.virtlink.vfs

/**
 * Specifies the kind of filename.
 */
enum class FilenameKind {
    /** Unspecified. */
    Unspecified,
    /** The filename refers to a file. */
    File,
    /** The filename refers to a folder. */
    Folder,
}