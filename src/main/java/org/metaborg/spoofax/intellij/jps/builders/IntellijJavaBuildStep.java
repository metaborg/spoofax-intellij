package org.metaborg.spoofax.intellij.jps.builders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettingsService;
import org.metaborg.spoofax.meta.core.SpoofaxMetaBuilder;

import java.util.Collections;
import java.util.List;

public final class IntellijJavaBuildStep extends BuildStep implements IJavaBuildStep {

    @Singleton
    public final static class IntellijJavaBuildStepDescriptor implements IBuildStepDescriptor {

        @Inject
        private IntellijJavaBuildStepDescriptor() {
        }

        @Override
        public String name() {
            return "Spoofax Java";
        }

        @Override
        public List<IBuildStepDescriptor> dependencies() {
            return Collections.emptyList();
        }

        @Override
        public IBuildStep createStep(IProject project) {
            return new IntellijJavaBuildStep(this, project);
        }
    }

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
