package org.metaborg.spoofax.intellij.project;

import com.google.inject.Inject;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import javax.annotation.Nullable;

/**
 * Created by daniel on 9/15/15.
 */
public class IntelliJProjectService implements IProjectService {

    private final IIntelliJResourceService resourceService;

    @Inject
    private IntelliJProjectService(IIntelliJResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Nullable
    @Override
    public IProject get(FileObject fileObject) {
        Project project = null;
        VirtualFile vFile = resourceService.unresolve(fileObject);
        //ProjectManager.getInstance().getOpenProjects()
        Module module = ModuleUtil.findModuleForFile(vFile, project);
        //Module module = ProjectRootManager.getInstance(project).getFileIndex().getModuleForFile(vFile);
        // And then return the IProject corresponding to the Module.
        return null;
    }
}
