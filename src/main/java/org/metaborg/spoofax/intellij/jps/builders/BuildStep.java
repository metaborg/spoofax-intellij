package org.metaborg.spoofax.intellij.jps.builders;

import com.google.common.base.Preconditions;
import org.metaborg.core.project.IProject;

public abstract class BuildStep implements IBuildStep {

    private final IBuildStepDescriptor descriptor;
    private final IProject project;

    @Override
    public IBuildStepDescriptor descriptor() {
        return this.descriptor;
    }

    @Override
    public IProject project() {
        return this.project;
    }

    /**
     * Initializes a new instance of the {@link BuildStep} class.
     * @param descriptor The build step descriptor.
     * @param project The project that is being built.
     */
    protected BuildStep(IBuildStepDescriptor descriptor, IProject project) {
        Preconditions.checkNotNull(descriptor);
        Preconditions.checkNotNull(project);

        this.descriptor = descriptor;
        this.project = project;
    }

    @Override
    public void onBuild() {

    }
}
