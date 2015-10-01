package org.metaborg.spoofax.intellij.jps.targetbuilders;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

public final class SpoofaxPreTargetType extends SpoofaxTargetType<SpoofaxPreTarget> {

    @Inject
    private SpoofaxPreTargetType(@NotNull final IJpsProjectService projectService) {
        super("spoofax-pre-production", projectService);
    }

    @NotNull
    public final SpoofaxPreTarget createTarget(@NotNull final SpoofaxJpsProject project) {
        return new SpoofaxPreTarget(project, this);
    }

}
