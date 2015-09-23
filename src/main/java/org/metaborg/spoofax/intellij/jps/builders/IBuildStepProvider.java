package org.metaborg.spoofax.intellij.jps.builders;

import org.metaborg.core.project.IProject;

import java.util.List;

/**
 * Provides build steps.
 */
public interface IBuildStepProvider {
    /**
     * Returns an ordered list of build steps for the specified project.
     * @param project The project.
     * @return The list of build steps to execute, from first to last.
     */
    List<IBuildStep> getBuildSteps(IProject project);
}
