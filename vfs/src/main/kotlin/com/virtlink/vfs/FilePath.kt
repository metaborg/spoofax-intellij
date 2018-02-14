package com.virtlink.vfs

import com.virtlink.vfs.text.PathEncoding
import java.io.Serializable
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
): Serializable {

    /**
     * @param normalizedComponents The normalized file path components.
     */
    private constructor(normalizedComponents: List<FilePathComponent>)
            : this(joinPath(normalizedComponents)){
        this._components = AtomicReference(normalizedComponents)
    }

    /**
     * @param components The file path components.
     */
    constructor(vararg components: FilePathComponent)
            : this(normalize(components.toList()))

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
     * Gets the path to the parent folder.
     *
     * For a file, this returns the path to the folder.
     * For a folder, this returns the path to the parent folder.
     *
     * Note that relative paths can have infinite parents
     * by adding `../` to the path.  Note that an absolute
     * path has no root, and therefore returns null.
     */
    val parent: FilePath? get() {
        return when (this.targetKind) {
            FilenameKind.File -> this.append(FilePath(com.virtlink.vfs.FilePathComponent.current))
            FilenameKind.Folder -> this.append(FilePath(com.virtlink.vfs.FilePathComponent.parent))
            FilenameKind.Unspecified -> FilePath()
        }
    }

    /**
     * Appends the specified path to this path.
     *
     * If [other] is an absolute path, [other] is returned.
     * Otherwise, [other] is treated as relative to this path.
     *
     * @param other The path to append to this path.
     * @return The resulting path.
     */
    fun append(other: FilePath): FilePath {
        if (other.isAbsolute) return other
        if (other.components.isEmpty()) return this

        return if (this.targetKind == FilenameKind.File)
            FilePath(normalize( this.components.take(this.components.count() - 1) + other.components))
        else
            FilePath(normalize(this.components + other.components))
    }

    override fun toString(): String = this._path

    companion object {
        /** The separator used between path components. */
        const val PATH_SEPARATOR = '/'
        /** The symbol used to refer to the current folder. */
        const val CURRENT_FOLDER = "."
        /** The symbol used to refer to the parent folder. */
        const val PARENT_FOLDER = ".."
        /** The default path encoding. */
        @JvmField val DEFAULT_PATH_ENCODING = PathEncoding()

        /**
         * Parses the given path into a [FilePath] object.
         *
         * @param path The path to parse.
         * @return The corresponding [FilePath] object.
         */
        fun parse(path: String): FilePath {
            val components = normalize(parsePath(path))
            return FilePath(components)
        }

        /**
         * Parses the path into a list of components.
         *
         * The path must be a valid VFS path, thus using `/` as the path separators,
         * with encoded names.
         *
         * The first path component of an absolute path has an empty string as its name.
         *
         * @param path The path to parse.
         * @return A list of path components.
         */
        private fun parsePath(path: String): List<FilePathComponent> {
            if (path.isEmpty()) return emptyList()

            // Get the indices of all separators in the path.
            val separators = path.mapIndexedNotNull { i, c ->
                if (c == FilePath.PATH_SEPARATOR) i else null
            }

            val componentCount = if (separators.isNotEmpty() && separators.last() == path.length - 1)
                separators.count()
            else
                separators.count() + 1

            val components = mutableListOf<FilePathComponent>()
            for (i in 0 until componentCount) {
                val start = if (i == 0) 0 else separators[i - 1] + 1
                val end = if (i == separators.count()) path.length else separators[i]

                val encodedName = path.substring(start, end)
                val isFolder = (i < separators.count())

                val kind = getKind(encodedName, isFolder)
                val component = toPathComponent(encodedName, kind)

                components.add(component)
            }
            return components
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
            // Only an empty component list may result in an empty component list.
            // Any other path, when normalized, at the very least results in `./`.
            if (components.isEmpty()) return emptyList()

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
                        if (started) throw IllegalStateException("Root component not allowed in the middle of the path.")
                        normalized.push(component)
                    }
                    FilePathComponentKind.Current -> {
                        // The current folder component `./` can be ignored unless it's the first component.
                        if (normalized.empty())
                            normalized.push(component)
                    }
                    FilePathComponentKind.Parent -> {
                        // The parent folder component `../` removes the top component
                        // from the stack (unless it is a root `/` component),
                        // except when the stack is empty, in which case
                        // we have to encode the parent folder component explicitly.
                        if (normalized.isNotEmpty() && !normalized.peek().isSpecial)
                            normalized.pop()
                        else if (normalized.isEmpty() || normalized.peek().kind != FilePathComponentKind.Root)
                            normalized.push(component)
                    }
                }
                started = true
            }

            // If the normalized path starts with a current folder component `./`
            // and it is not the only component, then it can be removed.
            val newComponents = normalized.toMutableList()
            if (newComponents.count() > 1 && newComponents[0] == FilePathComponent.current)
                newComponents.removeAt(0)

            return if (newComponents.isEmpty())
                listOf(FilePathComponent.current)
            else
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
         * Determines the component kind from the encoded name.
         *
         * @param encodedName The encoded name of the component.
         * @param isFolder Whether the component is a (root, current, parent) folder, or a file.
         * @return The component kind.
         */
        private fun getKind(encodedName: String, isFolder: Boolean): FilePathComponentKind {
            return when {
                encodedName.isEmpty() -> FilePathComponentKind.Root
                encodedName == FilePath.CURRENT_FOLDER -> FilePathComponentKind.Current
                encodedName == FilePath.PARENT_FOLDER -> FilePathComponentKind.Parent
                isFolder -> FilePathComponentKind.Folder
                else -> FilePathComponentKind.File
            }
        }

        /**
         * Gets the kind and encodes the name of the specified path component.
         *
         * @param component The path component.
         * @return A pair of the encoded name and the component kind.
         */
        private fun fromPathComponent(component: FilePathComponent): Pair<String, FilePathComponentKind> {
            val encodedName = when(component.kind) {
                FilePathComponentKind.Root -> ""
                FilePathComponentKind.Current -> FilePath.CURRENT_FOLDER
                FilePathComponentKind.Parent -> FilePath.PARENT_FOLDER
                FilePathComponentKind.Folder,
                FilePathComponentKind.File -> encode(component.name!!)
            }
            return Pair(encodedName, component.kind)
        }

        /**
         * Gets a path component from an encoded name and kind.
         *
         * @param encodedName The encoded name.
         * @param kind The path component kind.
         * @return The path component.
         */
        private fun toPathComponent(encodedName: String, kind: FilePathComponentKind): FilePathComponent {
            return when (kind) {
                FilePathComponentKind.Root -> FilePathComponent.root
                FilePathComponentKind.Current -> FilePathComponent.current
                FilePathComponentKind.Parent -> FilePathComponent.parent
                FilePathComponentKind.Folder -> FilePathComponent.folder(decode(encodedName))
                FilePathComponentKind.File -> FilePathComponent.file(decode(encodedName))
            }
        }

        /**
         * Encodes the specified un-encoded name for use in a path.
         *
         * @param unencoded The un-encoded name.
         * @return The encoded name.
         */
        private fun encode(unencoded: String): String
                = DEFAULT_PATH_ENCODING.encode(unencoded)

        /**
         * Decodes the specified encoded name for use in a path component.
         *
         * @param encoded The encoded name.
         * @return The decoded name.
         */
        private fun decode(encoded: String): String
                = DEFAULT_PATH_ENCODING.decode(encoded)

    }
}