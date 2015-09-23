package org.metaborg.spoofax.intellij.jps.builders;

import org.metaborg.core.project.IProject;

/**
 * Created by daniel on 9/22/15.
 */
public class IntellijJavaBuildStep extends BuildStep {
    /**
     * Initializes a new instance of the {@link BuildStep} class.
     *
     * @param descriptor The build step descriptor.
     * @param project    The project that is being built.
     */
    protected IntellijJavaBuildStep(IBuildStepDescriptor descriptor, IProject project) {
        super(descriptor, project);
    }
}
