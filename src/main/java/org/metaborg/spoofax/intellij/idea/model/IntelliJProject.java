package org.metaborg.spoofax.intellij.idea.model;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProject;

/**
 * An IntelliJ project.
 *
 * @author Daniël Pelsmaeker
 * @since 1.0
 */
public final class IntelliJProject implements IProject {

    @NotNull
    private final Module module;
    @NotNull
    private final FileObject location;

    @Inject
    private IntelliJProject(@Assisted @NotNull final Module module, @Assisted @NotNull final FileObject location) {
        this.module = module;
        this.location = location;
    }

    /**
     * Gets the IDE-specific module.
     *
     * @return The module.
     */
    @NotNull
    public final Module getModule() {
        return this.module;
    }

    /**
     * Gets the location of the project.
     *
     * @return The project root location.
     */
    @Override
    @NotNull
    public final FileObject location() {
        return this.location;
    }
}