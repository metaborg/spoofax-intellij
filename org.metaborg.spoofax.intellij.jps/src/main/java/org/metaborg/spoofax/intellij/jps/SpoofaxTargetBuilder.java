package org.metaborg.spoofax.intellij.jps;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.components.StoragePathMacros;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.jetbrains.jps.incremental.java.JavaBuilder;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.spoofax.intellij.*;

import java.io.IOException;
import java.util.Collections;

/**
 * Builds the Spoofax build target.
 */
@Singleton
public final class SpoofaxTargetBuilder extends TargetBuilder<SpoofaxSourceRootDescriptor, SpoofaxTarget> {

    private final ILanguageService languageService;

    @Inject
    private SpoofaxTargetBuilder(ILanguageService languageService, SpoofaxProductionTargetType targetType) {
        super(Collections.singletonList(targetType));
        this.languageService = languageService;
    }

    @Override
    public void buildStarted(CompileContext context) {
        JavaBuilder.IS_ENABLED.set(context, Boolean.FALSE);
    }

    @Override
    public void build(SpoofaxTarget target, DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxTarget> holder, BuildOutputConsumer outputConsumer, CompileContext context) throws ProjectBuildException, IOException {
        System.out.println(target.getOutputRoots(context));
        //File outputDirectory = getBuildOutputDirectory(target.getModule(), false, compileContext);
        context.processMessage(new ProgressMessage("Compiling Spoofax sources"));
        //buildSpoofax(target.getModule());
        context.checkCanceled();

        context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.INFO, "Using these languages: " + Joiner.on(", ").join(languageService.getAllLanguages())));

        context.processMessage(new CompilerMessage("Spoofax", BuildMessage.Kind.WARNING, "Compilation not implemented! (II)"));


        // https://github.com/pbuda/intellij-pony/blob/52e40c55d56adc4d85a34ad8dffe45ca0c64967f/jps-plugin/src/me/piotrbuda/intellij/pony/jps/PonyBuilder.java
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax Builder";
    }

    // https://github.com/pbuda/intellij-pony/blob/52e40c55d56adc4d85a34ad8dffe45ca0c64967f/jps-plugin/src/me/piotrbuda/intellij/pony/jps/PonyBuilder.java
    // https://github.com/RudyChin/platform_tools_adt_idea/blob/ac0e992c63f9962b3b3fb7417a398b641f2bb1dc/android/jps-plugin/src/org/jetbrains/jps/android/AndroidLibraryPackagingBuilder.java
    // https://github.com/pantsbuild/intellij-pants-plugin/blob/6fe84a536b4275358a38487f751fd64d6d5c9163/jps-plugin/com/twitter/intellij/pants/jps/incremental/PantsTargetBuilder.java
}