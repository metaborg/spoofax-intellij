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

    @NotNull
    private final SpoofaxPreTargetType preTargetType;

    // TODO: Inject!
    public SpoofaxPostTarget(@NotNull final SpoofaxJpsProject project,
                             @NotNull final SpoofaxPostTargetType targetType,
                             @NotNull final SpoofaxPreTargetType preTargetType) {
        super(project, targetType);
        this.preTargetType = preTargetType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isCompiledBeforeModuleLevelBuilders() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Collection<BuildTarget<?>> computeDependencies(@NotNull final BuildTargetRegistry buildTargetRegistry,
                                                                @NotNull final TargetOutputIndex targetOutputIndex) {
        final List<BuildTarget<?>> dependencies = new ArrayList<>();
        dependencies.add(new ModuleBuildTarget(super.myModule, JavaModuleBuildTargetType.PRODUCTION));
        dependencies.add(this.preTargetType.createTarget(super.myModule));
        return dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final String getPresentableName() {
        return "Spoofax POST target 2 '" + getId() + "'";
    }

}
