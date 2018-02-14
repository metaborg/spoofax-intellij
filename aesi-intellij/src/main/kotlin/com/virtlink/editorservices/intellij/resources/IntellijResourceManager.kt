package com.virtlink.editorservices.intellij.resources

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.virtlink.editorservices.resources.IContent
import com.virtlink.editorservices.resources.IResourceManager
import java.net.URI
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.testFramework.LightVirtualFile
import com.virtlink.editorservices.Offset
import com.virtlink.editorservices.Position
import com.virtlink.editorservices.content.StringContent
import com.virtlink.editorservices.resources.IAesiContent


/**
 * IntelliJ resource manager.
 *
 * This class provides access to the files, folders, and modules in an IntelliJ project,
 * and allows AESI services to get their attributes and content.
 */
@Suppress("PrivatePropertyName", "unused")
class IntellijResourceManager: IResourceManager {

    private val INTELLIJ_SCHEME = "intellij"
    private val MEM_SCHEME = "mem"

    // An IntelliJ URI is:
    // intellij:///project/module!/filepath
    // An in-memory URI is:
    // mem:///myfile
    // An absolute URI is, for example:
    // file:///myfolder/myfile

    fun getUri(document: Document, project: Project): URI {
        val psiFile = getPsiFile(document, project)
        if (psiFile != null) {
            return getUri(psiFile)
        } else {
            TODO()
        }
    }

    fun getUri(file: PsiFile): URI {
        val module = getModule(file)
//        if (module != null) {
        return getUri(file.originalFile.virtualFile, module, file.project)
//        } else {
//            return getUri(file.originalFile.virtualFile, null)
//        }
    }

    fun getUri(file: VirtualFile, project: Project): URI {
        val module = getModule(file, project)
        return getUri(file, module, project)
//
//        return if (module != null) {
//            getUri(file, module)
//        } else {
//            URI("$INTELLIJ_SCHEME:///${file.name}")
//        }
    }

//    fun getUri(file: VirtualFile, module: Module?): URI {
//        getUri(file, module, )
//        if (module != null) {
//            val modulePath = ModuleUtil.getModuleDirPath(module)
//            val moduleVirtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(modulePath)!!
//            val relativeFilePath = VfsUtil.getRelativePath(file, moduleVirtualFile)
//            if (relativeFilePath != null) {
//                // Path relative to the module.
//                return URI(getUri(module).toString() + "!/$relativeFilePath")
//            }
//        }
////        val modulePath = ModuleUtil.getModuleDirPath(module)
////        val moduleVirtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(modulePath)!!
////        val relativeFilePath = VfsUtil.getRelativePath(file, moduleVirtualFile)
////        return if (relativeFilePath != null) {
////            // Path relative to the module.
////            URI(getUri(module).toString() + "!/$relativeFilePath")
////        } else {
//            // Absolute path.
//            URI(file.url)
////        }
//    }

    /**
     * Gets the URI of the specified file in the specified module (or null)
     * in the specified project.
     *
     * @param file The file.
     * @param module The module; or null when not known.
     * @param project The project.
     * @return The URI of the file.
     */
    private fun getUri(file: VirtualFile, module: Module?, project: Project): URI {
        val relativeFilePath = if (module != null) {
            val modulePath = ModuleUtil.getModuleDirPath(module)
            val moduleVirtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(modulePath)!!
            VfsUtil.getRelativePath(file, moduleVirtualFile)
        } else null

        return if (relativeFilePath != null) {
            // Path relative to the module.
            URI("${getUri(module, project)}/$relativeFilePath")
        } else {
            // Absolute path.
            URI(file.url)
//            URI("${getUri(module, project)}/${file.name}")
        }
    }

    /**
     * Gets the URI of the module in a project.
     *
     * @param module The module; or null.
     * @return The URI of the module.
     */
    fun getUri(module: Module?, project: Project): URI {
        assert(module == null || module.project == project)
        return if (module != null) {
            URI("${getUri(module.project)}/${module.name}")
        } else {
            URI("${getUri(project)}/_")
        }
    }

    /**
     * Gets the URI of the project.
     *
     * @param project The project.
     * @return The URI of the project.
     */
    fun getUri(project: Project): URI {
        return URI("$INTELLIJ_SCHEME:///${project.name}")
    }

    /**
     * Gets the PSI file for the specified document in the specified project.
     *
     * @param document The document.
     * @param project The project.
     * @return The PSI file; or null when it could not be determined.
     */
    fun getPsiFile(document: Document, project: Project): PsiFile? {
        return PsiDocumentManager.getInstance(project).getPsiFile(document)
    }

    /**
     * Gets the PSI file for the specified URI.
     *
     * @param uri The URI.
     * @return The PSI file; or null when it could not be determined.
     */
    fun getPsiFile(uri: URI): PsiFile? {
        val result = parseUri(uri)

        if (result.module == null || result.file == null) {
            return null
        }
        return PsiManager.getInstance(result.module.project).findFile(result.file)
    }

    /**
     * Gets the virtual file for the specified URI.
     *
     * @param uri The URI.
     * @return The virtual file; or null when it could not be determined.
     */
    fun getFile(uri: URI): VirtualFile? {
        val result = parseUri(uri)
        return result.file
    }

    /**
     * Gets the module for the specified virtual file in the specified project.
     *
     * @param file The virtual file.
     * @param project The project.
     * @return The module; or null when it could not be determined.
     */
    fun getModule(file: VirtualFile, project: Project): Module? {
        var module = ModuleUtil.findModuleForFile(file, project)
        if (module == null && file is LightVirtualFile && file.originalFile != null) {
            module = ModuleUtil.findModuleForFile(file.originalFile, project)
        }
        return module
    }

    /**
     * Gets the module for the specified PSI file.
     *
     * @param psiFile The PSI file.
     * @return The module; or null when it could not be determined.
     */
    fun getModule(psiFile: PsiFile): Module? {
        return ModuleUtilCore.findModuleForPsiElement(psiFile)
        // From > IDEA 2017.2.5:
//        return ModuleUtilCore.findModuleForFile(psiFile)
    }

    /**
     * Gets the module for the specified PSI element.
     *
     * @param element The PSI element.
     * @return The module; or null when it could not be determined.
     */
    fun getModule(element: PsiElement): Module? {
        return ModuleUtil.findModuleForPsiElement(element)
    }

    /**
     * Gets the module that contains (or is) the specified URI.
     *
     * @param uri The URI.
     * @return The module; or null when it could not be determined.
     */
    fun getModule(uri: URI): Module? {
        val result = parseUri(uri)
        return result.module
    }

    /**
     * Gets the content roots of the specified module.
     *
     * Content roots won't overlap.
     *
     * @param module The module.
     * @return A list of virtual files representing the content roots of the module.
     */
    fun getContentRoots(module: Module): List<VirtualFile> {
        return ModuleRootManager.getInstance(module).contentRoots.toList()
    }

    /**
     * Parses the specified URI.
     *
     * @param uri The URI to parse.
     * @return The resulting components.
     */
    private fun parseUri(uri: URI): ParsedUrl {
        val module: Module?
        val file: VirtualFile?
        if (uri.scheme == INTELLIJ_SCHEME) {
            // intellij:///project/module/!/filepath
            val path = uri.path
            val modulePathSeparator = path.indexOf('!')
            val projectModulePart: String
            val filePart: String?
            if (modulePathSeparator >= 0) {
                // /project/module/!/filepath
                // A file or folder in a module.
                projectModulePart = path.substring(0, modulePathSeparator)
                filePart = path.substring(modulePathSeparator + 1).trimStart('/')
            } else {
                // /project/module/
                // A module a project
                projectModulePart = path
                filePart = null
            }
            module = parseModule(projectModulePart)
            file = getFileInModule(filePart, module)
        } else if (uri.scheme == MEM_SCHEME) {
            TODO()
        } else {
            module = null
            file = getFileInModule(uri.toString(), null)
        }
        return ParsedUrl(module, file)
    }

    /**
     * Parses the Project/Module combination.
     *
     * @param path The path, in the form `/projectname/modulename/`.
     */
    private fun parseModule(path: String): Module? {
        val trimmedPath = path.trim('/')
        val projectModuleSeparator = trimmedPath.indexOf('/')
        val projectName: String
        val moduleName: String?
        if (projectModuleSeparator > 0) {
            projectName = trimmedPath.substring(0, projectModuleSeparator)
            moduleName = trimmedPath.substring(projectModuleSeparator + 1)
        } else {
            projectName = trimmedPath
            moduleName = null
        }

        val project = getProjectByName(projectName)
        val module = if (project != null) getModuleByName(moduleName, project) else null
        return module
    }

    /**
     * Parses the file path.
     *
     * @param uri The relative path or absolute URI, in the form `/myfolder/myfile`.
     * @param module The module.
     * @return The virtual file; or null when not found.
     */
    private fun getFileInModule(uri: String?, module: Module?): VirtualFile? {
        if (uri == null) return null

        val moduleVirtualFile: VirtualFile?
        if (module != null) {
            val modulePath = ModuleUtil.getModuleDirPath(module)
            moduleVirtualFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(modulePath)
        } else {
            moduleVirtualFile = null
        }
        return VfsUtil.findRelativeFile(uri, moduleVirtualFile)
    }

    /**
     * Gets a project by name.
     *
     * @param name The project name.
     * @return The project; or null when not found.
     */
    private fun getProjectByName(name: String?): Project? {
        if (name == null) return null

        return ProjectManager.getInstance().openProjects.singleOrNull { p -> p.name == name }
    }

    /**
     * Gets a module by name.
     *
     * @param name The name to look for.
     * @param project The project that contains the module.
     * @return The module; or null when not found.
     */
    private fun getModuleByName(name: String?, project: Project): Module? {
        if (name == null) return null

        return ModuleManager.getInstance(project).findModuleByName(name)
    }

    override fun getProjectOf(uri: URI): URI? {
        val module = getModule(uri)
        return if (module != null) getUri(module, module.project) else module
    }

    override fun isProject(uri: URI): Boolean {
        val (module, file) = parseUri(uri)
        return module != null && file == null
    }

    override fun isFolder(uri: URI): Boolean {
        val virtualFile = getFile(uri)
        return virtualFile != null && virtualFile.isDirectory
    }

    override fun isFile(uri: URI): Boolean {
        val virtualFile = getFile(uri)
        return virtualFile != null && !virtualFile.isDirectory
    }

    override fun exists(uri: URI): Boolean {
        val virtualFile = getFile(uri)
        return virtualFile != null && virtualFile.exists()
    }

    override fun getChildren(uri: URI): Iterable<URI>? {
        val (module, file) = parseUri(uri)
        if (module == null || file == null) return null
        return file.children.map { f -> getUri(f, module, module.project) }
    }

    override fun getParent(uri: URI): URI? {
        // This is probably not correct in all situations.
        return if (uri.path.endsWith("/")) uri.resolve("..") else uri.resolve(".")
    }


    override fun getContent(uri: URI): IContent? {
        val psiFile = getPsiFile(uri)
        val document: Document?
        document = if (psiFile != null) {
            PsiDocumentManager.getInstance(psiFile.project).getDocument(psiFile)
        } else {
            val virtualFile = getFile(uri) ?: return null
            FileDocumentManager.getInstance().getDocument(virtualFile)
        }
        return if (document != null) StringContent(document.text, document.modificationStamp) else null
    }

    override fun getOffset(content: IContent, position: Position): Offset? {
        return (content as? IAesiContent)?.getOffset(position)
    }

    override fun getPosition(content: IContent, offset: Offset): Position? {
        return (content as? IAesiContent)?.getPosition(offset)
    }

    private data class ParsedUrl(
            val module: Module?,
            val file: VirtualFile?
    )
}