package org.metaborg.spoofax.intellij.idea.model;

import com.google.common.base.Preconditions;
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

    @NotNull private final Module ideModule;
    @NotNull private final FileObject location;

    /**
     * Gets the IDE-specific project object.
     * @return The IDE's project object.
     */
    @NotNull public final Module getIdeModule() { return this.ideModule; }

    @Override
    @NotNull public final FileObject location() {
        return this.location;
    }

    /**
     * Creates a new instance of the {@link IntelliJProject} class.
     *
     * Don't call this constructor directly. Instead, use {@link ProjectFactory#create(Module)}.
     *
     * @param ideModule The IDE-specific module object.
     * @param location The project root location.
     */
    public IntelliJProject(@NotNull final Module ideModule, @NotNull final FileObject location)
    {
        this.ideModule = ideModule;
        this.location = location;
    }
}