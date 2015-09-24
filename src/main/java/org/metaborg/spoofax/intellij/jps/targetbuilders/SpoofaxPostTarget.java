package org.metaborg.spoofax.intellij.jps.targetbuilders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTarget;
import org.jetbrains.jps.builders.BuildTargetRegistry;
import org.jetbrains.jps.builders.TargetOutputIndex;
import org.jetbrains.jps.builders.java.JavaModuleBuildTargetType;
import org.jetbrains.jps.incremental.ModuleBuildTarget;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Build target for Spoofax projects post-Java.
 */
public final class SpoofaxPostTarget extends SpoofaxTarget {

    private final SpoofaxPreTargetType preTargetType;

    public SpoofaxPostTarget(@NotNull SpoofaxJpsProject project, @NotNull SpoofaxPostTargetType targetType, @NotNull SpoofaxPreTargetType preTargetType) {
        super(project, targetType);
        this.preTargetType = preTargetType;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax POST target 2 '" + getId() + "'";
    }

    @Override
    public boolean isCompiledBeforeModuleLevelBuilders() {
        return false;
    }

    @Override
    public Collection<BuildTarget<?>> computeDependencies(BuildTargetRegistry buildTargetRegistry, TargetOutputIndex targetOutputIndex) {
        final List<BuildTarget<?>> dependencies = new ArrayList<>();
        dependencies.add(new ModuleBuildTarget(super.myModule, JavaModuleBuildTargetType.PRODUCTION));
        dependencies.add(this.preTargetType.createTarget(super.myModule));
        return dependencies;
    }

}
