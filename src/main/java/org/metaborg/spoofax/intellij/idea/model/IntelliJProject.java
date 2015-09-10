package org.metaborg.spoofax.intellij.idea.model;

import com.google.common.base.Preconditions;
import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.project.IProject;

/**
 * An IntelliJ project.
 *
 * @author Daniël Pelsmaeker
 * @since 1.0
 */
public class IntelliJProject implements IProject {

    private final Module ideModule;
    private final FileObject location;

    /**
     * Gets the IDE-specific project object.
     * @return The IDE's project object.
     */
    public Module getIdeModule() { return this.ideModule; }

    @Override
    public FileObject location() {
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
    public IntelliJProject(Module ideModule, FileObject location)
    {
        Preconditions.checkNotNull(ideModule);
        Preconditions.checkNotNull(location);

        this.ideModule = ideModule;
        this.location = location;
    }
}