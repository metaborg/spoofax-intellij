package org.metaborg.spoofax.intellij.project;

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProject;

// TODO: Move this to Spoofax core?

/**
 * A language project represented by a Spoofax artifact (*.spoofax-language).
 */
public final class ArtifactProject implements IProject {

    @NotNull
    private final FileObject location;

    /**
     * Initializes a new instance of the {@link ArtifactProject} class.
     *
     * @param location The location of the artifact's archive.
     */
    public ArtifactProject(@NotNull final FileObject location) {
        this.location = location;
    }

    @Override
    @NotNull
    public FileObject location() {
        return this.location;
    }
}
