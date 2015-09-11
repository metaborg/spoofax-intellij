package org.metaborg.spoofax.intellij.jps;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.project.IProject;

public class SpoofaxProject implements IProject {

    private final FileObject location;

    public SpoofaxProject(FileObject location)
    {
        this.location = location;
    }

    @Override
    public FileObject location() {
        return this.location;
    }
}
