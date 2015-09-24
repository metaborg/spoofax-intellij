package org.metaborg.spoofax.intellij.jps.targets;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

public final class SpoofaxNewPostTargetType extends SpoofaxNewTargetType<SpoofaxNewPostTarget> {

    private final SpoofaxNewPreTargetType preTargetType;

    @Inject
    private SpoofaxNewPostTargetType(@NotNull IJpsProjectService projectService, @NotNull SpoofaxNewPreTargetType preTargetType) {
        super("spoofax-post-production", projectService);
        this.preTargetType = preTargetType;
    }

    @NotNull
    public SpoofaxNewPostTarget createTarget(@NotNull SpoofaxJpsProject project) {
        return new SpoofaxNewPostTarget(project, this, this.preTargetType);
    }

}
