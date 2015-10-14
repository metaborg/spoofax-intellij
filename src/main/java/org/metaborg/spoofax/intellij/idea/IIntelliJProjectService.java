package org.metaborg.spoofax.intellij.idea;

import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProjectService;
import org.metaborg.spoofax.intellij.idea.model.IntelliJProject;

import javax.annotation.Nullable;

/**
 * Project service for IntelliJ.
 */
public interface IIntelliJProjectService extends IProjectService {

    /**
     * Indicates to the project service that a Spoofax project was opened.
     *
     * @param project The Spoofax project that was opened.
     */
    void open(@NotNull IntelliJProject project);

    /**
     * Indicates to the project service that a Spoofax project was closed.
     *
     * @param module The IntelliJ module of the project.
     */
    void close(@NotNull Module module);

    /**
     * Retrieves the Spoofax project of a given IntelliJ module.
     *
     * @param module The IntelliJ module.
     * @return The corresponding Spoofax project;
     * or <code>null</code> if no project could be found.
     */
    @Nullable
    IntelliJProject get(Module module);

}
