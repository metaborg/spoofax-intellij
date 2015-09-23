package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.spoofax.intellij.*;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Builds the Spoofax build target.
 */
@Singleton
public final class SpoofaxPreBuilder extends SpoofaxBuilder<SpoofaxPreTarget> {


    @Inject
    private SpoofaxPreBuilder(SpoofaxPreTargetType targetType) {
        super(targetType);
    }


}