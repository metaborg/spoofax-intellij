package org.metaborg.spoofax.intellij.jps.builders;

import org.metaborg.core.project.IProject;

import java.util.List;

/**
 * Describes a step in the build process.
 */
public interface IBuildStepDescriptor {
    /**
     * Gets the name of the build step.
     * @return The name of the build step.
     */
    String name();

    /**
     * Gets a list of build steps that must be executed before this one.
     * @return A list of build steps.
     */
    List<IBuildStepDescriptor> dependencies();

    /**
     * Creates a build step for the specified project.
     * @param project The project.
     * @return The build step.
     */
    IBuildStep createStep(IProject project);
}
