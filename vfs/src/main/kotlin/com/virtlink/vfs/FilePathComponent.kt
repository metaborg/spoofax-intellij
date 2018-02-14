package com.virtlink.vfs

import java.io.Serializable

/**
 * A single component in a [FilePath].
 *
 * @property name Gets the name of the component; or an empty string when it is a root component,
 * or null when it is a parent folder or current folder component.
 * @property kind Gets the kind of component.
 */
data class FilePathComponent private constructor(
        val name: String?,
        val kind: FilePathComponentKind
): Serializable {

    companion object {

        /**
         * Gets the root path component (i.e. `/`).
         */
        @JvmField val root = FilePathComponent("", FilePathComponentKind.Root)

        /**
         * Gets the current path component (i.e. `./`).
         */
        @JvmField val current = FilePathComponent(null, FilePathComponentKind.Current)

        /**
         * Gets the parent path component (i.e. `../`).
         */
        @JvmField val parent = FilePathComponent(null, FilePathComponentKind.Parent)

        /**
         * Creates a folder path component with the specified name.
         *
         * @param name The un-encoded name of the folder.
         * @return The folder path component.
         */
        @JvmStatic fun folder(name: String): FilePathComponent {
            if (name.isEmpty())
                throw IllegalArgumentException("Argument 'name' must be a non-empty string.")

            return FilePathComponent(name, FilePathComponentKind.Folder)
        }

        /**
         * Creates a file path component with the specified name.
         *
         * @param name The un-encoded name of the file.
         * @return The file path component.
         */
        @JvmStatic fun file(name: String): FilePathComponent {
            if (name.isEmpty())
                throw IllegalArgumentException("Argument 'name' must be a non-empty string.")

            return FilePathComponent(name, FilePathComponentKind.File)
        }
    }

    /**
     * Gets whether the path component is special.
     *
     * A special path component denotes the root of a file system (i.e. `/`),
     * a parent folder (i.e. `../`), or the current folder (i.e. `./`).
     */
    val isSpecial get() = this.kind != FilePathComponentKind.File && this.kind != FilePathComponentKind.Folder

    override fun toString(): String {
        return when (this.kind) {
            FilePathComponentKind.Current -> FilePath.CURRENT_FOLDER + FilePath.PATH_SEPARATOR
            FilePathComponentKind.Parent -> FilePath.PARENT_FOLDER + FilePath.PATH_SEPARATOR
            FilePathComponentKind.Root -> "" + FilePath.PATH_SEPARATOR
            FilePathComponentKind.Folder -> this.name!! + FilePath.PATH_SEPARATOR
            FilePathComponentKind.File -> this.name!!
        }
    }
}