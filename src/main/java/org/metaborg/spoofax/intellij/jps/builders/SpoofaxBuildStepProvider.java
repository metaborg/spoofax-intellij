package org.metaborg.spoofax.intellij.jps.builders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Singleton
public final class SpoofaxBuildStepProvider extends BuildStepProvider {

    private final Collection<IBuildStepDescriptor> rootSteps;

    @Inject
    private SpoofaxBuildStepProvider(@NotNull Collection<IBuildStepDescriptor> rootSteps) {
        this.rootSteps = rootSteps;
    }

    @Override
    public List<IBuildStep> getBuildSteps(IProject project) {
        return getBuildSteps(project, this.rootSteps);
    }
}
