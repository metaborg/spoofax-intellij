package org.metaborg.spoofax.intellij.jps.targetbuilders;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

public final class SpoofaxPostTargetType extends SpoofaxTargetType<SpoofaxPostTarget> {

    private final SpoofaxPreTargetType preTargetType;

    @Inject
    private SpoofaxPostTargetType(@NotNull IJpsProjectService projectService, @NotNull SpoofaxPreTargetType preTargetType) {
        super("spoofax-post-production", projectService);
        this.preTargetType = preTargetType;
    }

    @NotNull
    public SpoofaxPostTarget createTarget(@NotNull SpoofaxJpsProject project) {
        return new SpoofaxPostTarget(project, this, this.preTargetType);
    }

}
