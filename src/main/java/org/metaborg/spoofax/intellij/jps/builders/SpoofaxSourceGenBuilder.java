package org.metaborg.spoofax.intellij.jps.builders;

import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.java.JavaSourceRootDescriptor;
import org.jetbrains.jps.incremental.*;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;

import java.io.IOException;

/**
 * Created by daniel on 9/22/15.
 */
@Singleton
public final class SpoofaxSourceGenBuilder extends ModuleLevelBuilder {

    public SpoofaxSourceGenBuilder() {
        super(BuilderCategory.SOURCE_GENERATOR);
    }

    @Override
    public ExitCode build(CompileContext context, ModuleChunk chunk, DirtyFilesHolder<JavaSourceRootDescriptor, ModuleBuildTarget> holder, OutputConsumer consumer) throws ProjectBuildException, IOException {
        context.processMessage(new CompilerMessage("Spoofax src gen", BuildMessage.Kind.WARNING, "Called!"));
        return ExitCode.OK;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax source gen";
    }
}
