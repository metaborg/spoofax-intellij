package org.metaborg.spoofax.intellij.jps.builders;

import com.google.inject.Singleton;
import org.metaborg.core.project.IProject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Singleton
public final class SpoofaxBuildStepProvider extends BuildStepProvider {
    private static final Collection<IBuildStepDescriptor> rootSteps = Arrays.asList(
            /* TODO: List of root build steps here! */
    );

    @Override
    public List<IBuildStep> getBuildSteps(IProject project) {
        return getBuildSteps(project, this.rootSteps);
    }
}
