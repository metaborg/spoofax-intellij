package org.metaborg.spoofax.intellij;

import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.project.IProject;

/**
 * A module in IntelliJ IDEA.
 */
public final class IdeaModule implements IProject {

    private final Module ideaModule;
    private final FileObject location;

    /**
     * Initializes a new instance of the {@link IdeaModule} class.
     * @param ideaModule The IntelliJ IDEA module object.
     * @param location The location of the root of the module.
     */
    public IdeaModule(final Module ideaModule, final FileObject location) {
        this.ideaModule = ideaModule;
        this.location = location;
    }

    /**
     * Gets the IntelliJ IDEA module object.
     * @return The IntelliJ IDEA module object.
     */
    public Module module() { return this.ideaModule;}

    @Override
    public FileObject location() {
        return this.location;
    }
}
