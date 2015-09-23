package org.metaborg.spoofax.intellij.jps.builders;

import org.metaborg.core.project.IProject;

/**
 * A step in the build process.
 */
public interface IBuildStep {

    /**
     * Gets the descriptor.
     * @return The descriptor.
     */
    IBuildStepDescriptor descriptor();

    /**
     * Gets the project being built.
     * @return The project being built.
     */
    IProject project();

    void onBuild();

}
