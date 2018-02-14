package com.virtlink.vfs

import java.util.*
import java.util.concurrent.atomic.AtomicReference

/**
 * A path.
 *
 * @property path Gets the encoded absolute or relative path, separated by path separators.
 * The path is absolute if-and-only-if it starts with a path separator.
 */
data class FilePath private constructor(
        val _path: String
) {

    companion object {
        /** The separator used between path components. */
        const val PATH_SEPARATOR = '/'
        /** The symbol used to refer to the current folder. */
        const val CURRENT_FOLDER = "."
        /** The symbol used to refer to the parent folder. */
        const val PARENT_FOLDER = ".."
    }


    constructor(components: List<FilePathComponent>) {

    }

    constructor(vararg components: FilePathComponent)
            : this(components.toList())

    /**
     * Cache with the list of components in the path;
     * or null when they have not been determined.
     */
    private var _components: AtomicReference<List<FilePathComponent>?> = AtomicReference(null)

    /**
     * Gets the list of components in the path.
     */
    val components: List<FilePathComponent> get() {
        if (this._components.get() == null) {
            // Atomic set.
            val components = parsePath(this._path)
            this._components.compareAndSet(null, components)
            // Either `_components` was successfully set to our new value;
            // or it was set by some other thread.  In both cases the
            // result is the same.  Using `compareAndSet()` (instead of just
            // `set()`) ensures that calls to the `components` property
            // always get the same `FilePathComponent` object instances.
        }
        assert(this._components.get() != null)
        return this._components.get()!!
    }

    /**
     * Gets the kind of target the path refers to.
     */
    val targetKind: FilenameKind get() {
        // An empty path can refer to anything
        if (this._path.isEmpty()) return FilenameKind.Unspecified

        return when (this.components.last().kind) {
            FilePathComponentKind.Root,
            FilePathComponentKind.Current,
            FilePathComponentKind.Parent,
            FilePathComponentKind.Folder -> FilenameKind.Folder
            FilePathComponentKind.File -> FilenameKind.File
        }
    }

    /**
     * Gets the kind of path, absolute or relative.
     *
     * A path is absolute when it starts with a path separator.
     */
    val kind: FilePathKind get() {
        return if (this._path.isNotEmpty() && this._path[0] == FilePath.PATH_SEPARATOR)
            FilePathKind.Absolute
        else
            FilePathKind.Relative
    }

    /**
     * Gets whether the path is absolute.
     *
     * A path is absolute when it starts with a path separator.
     */
    val isAbsolute: Boolean get() = this.kind == FilePathKind.Absolute

    /**
     * Gets whether the path is relative.
     */
    val isRelative: Boolean get() = this.kind == FilePathKind.Relative

    /**
     * Gets the path to the parent folder;
     * or null when the path has no parent folder.
     */
    val parent: FilePath? get() {
        val components = this.components
        if (components.count() <= 1) return null
        return FilePath(components.take(components.count() - 1))
    }


    /**
     * Parses the path into a list of components.
     *
     * The first path component of an absolute path has an empty string as its name.
     *
     * @param path The path to parse, without a trailing separator.
     * @return A list of path components.
     */
    private fun parsePath(path: String): List<FilePathComponent> {
        TODO()
    }

    /**
     * Normalizes the specified list of path components.
     *
     * Normalization simplifies the path. For example:
     *     foo/bar/./      becomes    foo/bar/
     *     foo/./bar       becomes    foo/bar
     *     foo/bar/../     becomes    foo/
     *     ../foo/../      becomes    ../
     *     ./              stays      ./
     *
     * @param components The path components to normalize.
     * @return The normalized path components.
     */
    private fun normalize(components: List<FilePathComponent>): List<FilePathComponent> {
        val normalized = Stack<FilePathComponent>()
        var started = false
        var finished = false

        for (component in components) {
            when (component.kind) {
                FilePathComponentKind.Folder -> {
                    // The folder component is allowed anywhere.
                    normalized.push(component)
                }
                FilePathComponentKind.File -> {
                    // The file component is only allowed as the last component.
                    if (finished) throw IllegalStateException("File component not allowed in the middle of the path.")
                    normalized.push(component)
                    finished = true
                }
                FilePathComponentKind.Root -> {
                    // The root component `/` is only allowed as the first component.
                    if (started) throw IllegalStateException("Root component not allowed in the middle of the path.");
                    normalized.push(component)
                }
                FilePathComponentKind.Current -> {
                    // The current folder component `./` can be ignored unless it's the first component.
                    if (normalized.empty())
                        normalized.push(component)
                }
                FilePathComponentKind.Parent -> {
                    // The parent folder component `../` removes the top component
                    // from the stack, unless the stack is empty, in which case
                    // we have to encode the parent folder component explicitly.
                    if (normalized.isNotEmpty() && !normalized.peek().isSpecial)
                        normalized.pop()
                    else
                        normalized.push(component)
                }
            }
            started = true
        }

        val newComponents = normalized.asReversed()

        if (newComponents.count() > 1 && newComponents[0] == FilePathComponent.current)
            newComponents.removeAt(0)

        return newComponents
    }

    /**
     * Joins a list of path components into a single string.
     *
     * @param components The path components to join.
     * @return The joined path.
     */
    private fun joinPath(components: List<FilePathComponent>): String {
        val sb = StringBuilder()

        for(component in components) {
            val (encodedName, kind) = fromPathComponent(component)
            sb.append(encodedName)

            val isFolder = (kind != FilePathComponentKind.File)
            if (isFolder)
                sb.append(FilePath.PATH_SEPARATOR)
        }

        return sb.toString()
    }

    /**
     * Gets the encoded name and component kind from the specified path component.
     *
     * @param component The path component.
     * @return A pair of the encoded name and the component kind.
     */
    private fun fromPathComponent(component: FilePathComponent): Pair<String, FilePathComponentKind> {
        val encodedName = when(component.kind) {
            FilePathComponentKind.Current -> FilePath.CURRENT_FOLDER
            FilePathComponentKind.Parent -> FilePath.PARENT_FOLDER
            FilePathComponentKind.Root -> ""
            FilePathComponentKind.Folder,
            FilePathComponentKind.File -> encode(component.name!!)
        }
        return Pair(encodedName, component.kind)
    }

    /**
     * Encodes the specified un-encoded name for use in a path.
     *
     * @param unencoded The un-encoded name.
     * @return The encoded name.
     */
    private fun encode(unencoded: String): String {
       TODO()
    }

}