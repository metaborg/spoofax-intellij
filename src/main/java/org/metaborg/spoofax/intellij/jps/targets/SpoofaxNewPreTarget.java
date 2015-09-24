package org.metaborg.spoofax.intellij.jps.targets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTarget;
import org.jetbrains.jps.builders.BuildTargetRegistry;
import org.jetbrains.jps.builders.TargetOutputIndex;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Build target for Spoofax projects pre-Java.
 */
public final class SpoofaxNewPreTarget extends SpoofaxNewTarget {

    public SpoofaxNewPreTarget(@NotNull SpoofaxJpsProject project, @NotNull List<IBuildStep> steps, @NotNull SpoofaxNewPreTargetType targetType) {
        super(project, steps, targetType);
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax PRE target 2 '" + getId() + "'";
    }

    @Override
    public boolean isCompiledBeforeModuleLevelBuilders() {
        return true;
    }

    @Override
    public Collection<BuildTarget<?>> computeDependencies(BuildTargetRegistry buildTargetRegistry, TargetOutputIndex targetOutputIndex) {
        return Collections.emptyList();
    }

}
