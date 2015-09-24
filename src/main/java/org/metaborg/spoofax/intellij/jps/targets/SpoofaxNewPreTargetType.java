package org.metaborg.spoofax.intellij.jps.targets;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

public final class SpoofaxNewPreTargetType extends SpoofaxNewTargetType<SpoofaxNewPreTarget> {

    @Inject
    private SpoofaxNewPreTargetType(@NotNull IJpsProjectService projectService) {
        super("spoofax-pre-production", projectService);
    }

    @NotNull
    public SpoofaxNewPreTarget createTarget(@NotNull SpoofaxJpsProject project) {
        return new SpoofaxNewPreTarget(project, this);
    }

}
