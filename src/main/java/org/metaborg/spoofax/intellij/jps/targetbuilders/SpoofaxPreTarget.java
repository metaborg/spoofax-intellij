package org.metaborg.spoofax.intellij.jps.targetbuilders;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTarget;
import org.jetbrains.jps.builders.BuildTargetRegistry;
import org.jetbrains.jps.builders.TargetOutputIndex;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;

import java.util.Collection;
import java.util.Collections;

/**
 * Build target for Spoofax projects pre-Java.
 */
public final class SpoofaxPreTarget extends SpoofaxTarget {

    // TODO: Inject!
    public SpoofaxPreTarget(@NotNull final SpoofaxJpsProject project, @NotNull final SpoofaxPreTargetType targetType) {
        super(project, targetType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isCompiledBeforeModuleLevelBuilders() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Collection<BuildTarget<?>> computeDependencies(@NotNull final BuildTargetRegistry buildTargetRegistry,
                                                                @NotNull final TargetOutputIndex targetOutputIndex) {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public final String getPresentableName() {
        return "Spoofax PRE target 2 '" + getId() + "'";
    }

}
