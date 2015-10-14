package org.metaborg.spoofax.intellij.factories;

import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.model.IntelliJProject;

/**
 * Factory for projects.
 */
public interface IProjectFactory {

    /**
     * Creates a new project.
     *
     * @param intellijModule The IntelliJ module.
     * @param contentPath    Where the module lives.
     * @return The created project.
     */
    @NotNull
    IntelliJProject create(@NotNull Module intellijModule, @NotNull FileObject contentPath);

}
