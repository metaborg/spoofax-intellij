package org.metaborg.spoofax.intellij;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsTypedModule;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepDescriptor;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepProvider;
import org.metaborg.spoofax.intellij.jps.builders.InitializationBuildStep;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxProject;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * The Spoofax pre-Java build target type.
 */
@Singleton
public final class SpoofaxPreTargetType extends SpoofaxTargetType<SpoofaxPreTarget> {

    @Inject
    private SpoofaxPreTargetType(IBuildStepProvider buildStepProvider, IIntelliJResourceService resourceService) {
        super(SpoofaxPreTargetType.class.getName(), buildStepProvider, resourceService);
    }

    @NotNull
    @Override
    public SpoofaxPreTarget createTarget(IProject project, JpsTypedModule<JpsDummyElement> module) {
        List<IBuildStep> steps = this.buildStepProvider.getBuildSteps(project);
        // TODO: Filter steps to only those before Java
        return new SpoofaxPreTarget(project, steps, module, this);
    }
}
