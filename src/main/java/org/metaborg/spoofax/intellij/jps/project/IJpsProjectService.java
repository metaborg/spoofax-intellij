package org.metaborg.spoofax.intellij.jps.project;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProjectService;

import javax.annotation.Nullable;

/**
 * A project service for JPS.
 */
public interface IJpsProjectService extends IProjectService {

    /**
     * Creates and adds a new project for the specified JPS module.
     *
     * @param module The JPS module.
     * @return The created project.
     */
    @NotNull
    SpoofaxJpsProject create(@NotNull JpsModule module);

    /**
     * Finds the project corresponding to the specified module.
     *
     * @param module The JPS module to look for.
     * @return The project that corresponds to the JPS module;
     * or <code>null</code> when not found.
     */
    @Nullable
    SpoofaxJpsProject get(@NotNull JpsModule module);

}
