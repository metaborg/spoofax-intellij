package org.metaborg.spoofax.intellij.jps.targetbuilders;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;


public final class SpoofaxPostTargetType extends SpoofaxTargetType<SpoofaxPostTarget> {

    @NotNull
    private final SpoofaxPreTargetType preTargetType;

    @Inject
    private SpoofaxPostTargetType(@NotNull final IJpsProjectService projectService,
                                  @NotNull final SpoofaxPreTargetType preTargetType) {
        super("spoofax-post-production", projectService);
        this.preTargetType = preTargetType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public final SpoofaxPostTarget createTarget(@NotNull final SpoofaxJpsProject project) {
        return new SpoofaxPostTarget(project, this, this.preTargetType);
    }

}
