package org.metaborg.spoofax.intellij.jps.project;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.project.IProject;

/**
 * A Spoofax project used in JPS.
 */
public final class SpoofaxJpsProject implements IProject {

    private final FileObject location;

    /**
     * Initializes a new instance of the {@link SpoofaxJpsProject} class.
     * @param location The location of the project root.
     */
    public SpoofaxJpsProject(FileObject location) {
        this.location = location;
    }

    @Override
    public FileObject location() {
        return this.location;
    }
}
