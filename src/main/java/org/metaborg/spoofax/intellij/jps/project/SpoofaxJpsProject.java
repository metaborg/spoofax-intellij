package org.metaborg.spoofax.intellij.jps.project;

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProject;

/**
 * A Spoofax project used in JPS.
 */
public final class SpoofaxJpsProject implements IProject {

    private final FileObject location;
    private final JpsModule module;

    @Override
    public FileObject location() {
        return this.location;
    }

    public JpsModule module() { return this.module; }

    /**
     * Initializes a new instance of the {@link SpoofaxJpsProject} class.
     * @param location The location of the project root.
     */
    public SpoofaxJpsProject(JpsModule module, FileObject location) {
        this.module = module;
        // TODO: Get location from JpsModule?
        // NOTE: A module can have multiple content roots, or none at all.
        // Instead of trying to do everything relative to the project root,
        // we should use explicit directories for generated files and such.
        // Those would be configurable in the IDE, and can be found in the
        // content roots list as directories for generated files. Then we'd
        // simply pick the first, or error when there are none.
        //String projectRoot = this.module.getContentRootsList().getUrls().get(0);
        //return new SpoofaxJpsProject(this.resourceService.resolve(projectRoot));

        this.location = location;
    }

}
