package org.metaborg.spoofax.intellij.jps.project;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.jps.model.JpsUrlList;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProjectService;
import org.metaborg.core.resource.IResourceService;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A project service for JPS.
 */
public interface IJpsProjectService extends IProjectService {

//    /**
//     * Adds a project to this service.
//     *
//     * @param project The project to add.
//     */
//    void add(SpoofaxJpsProject project);

    /**
     * Creates and adds a new project for the specified JPS module.
     *
     * @param module The JPS module.
     * @return The created project.
     */
    SpoofaxJpsProject create(JpsModule module);

    /**
     * Finds the project corresponding to the specified module.
     *
     * @param module The JPS module to look for.
     * @return The project that corresponds to the JPS module;
     * or <code>null</code> when not found.
     */
    @Nullable
    SpoofaxJpsProject get(JpsModule module);

}
