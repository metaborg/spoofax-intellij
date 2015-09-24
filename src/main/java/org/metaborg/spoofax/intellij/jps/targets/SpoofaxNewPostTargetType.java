package org.metaborg.spoofax.intellij.jps.targets;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.project.IJpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

import java.util.Collections;
import java.util.List;

public final class SpoofaxNewPostTargetType extends SpoofaxNewTargetType<SpoofaxNewPostTarget> {

    //public static final SpoofaxNewPostTargetType INSTANCE = new SpoofaxNewPostTargetType();   // TODO: Refactor
    private final SpoofaxNewPreTargetType preTargetType;

    @Inject
    private SpoofaxNewPostTargetType(@NotNull IJpsProjectService projectService, @NotNull SpoofaxNewPreTargetType preTargetType) {
        super("spoofax-post-production", projectService);
        this.preTargetType = preTargetType;
    }

    @NotNull
    public SpoofaxNewPostTarget createTarget(@NotNull SpoofaxJpsProject project) {
        List<IBuildStep> steps = Collections.emptyList(); // TODO
        return new SpoofaxNewPostTarget(project, steps, this, this.preTargetType);
    }

}
