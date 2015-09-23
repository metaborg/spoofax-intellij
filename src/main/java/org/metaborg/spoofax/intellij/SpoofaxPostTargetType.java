package org.metaborg.spoofax.intellij;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsTypedModule;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStep;
import org.metaborg.spoofax.intellij.jps.builders.IBuildStepProvider;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import java.util.ArrayList;
import java.util.List;

/**
 * The Spoofax build target type.
 */
@Singleton
public final class SpoofaxPostTargetType extends SpoofaxTargetType<SpoofaxPostTarget> {

    private final SpoofaxPreTargetType preTargetType;

    @Inject
    private SpoofaxPostTargetType(SpoofaxPreTargetType preTargetType, IBuildStepProvider buildStepProvider, IIntelliJResourceService resourceService) {
        super(SpoofaxPreTargetType.class.getName(), buildStepProvider, resourceService);
        this.preTargetType = preTargetType;
    }

    @NotNull
    @Override
    public SpoofaxPostTarget createTarget(IProject project, JpsTypedModule<JpsDummyElement> module) {
        List<IBuildStep> steps = this.buildStepProvider.getBuildSteps(project);
        // TODO: Filter steps to only those after Java
        return new SpoofaxPostTarget(preTargetType, project, steps, module, this);
    }


    //public static final SpoofaxPostTargetType PRODUCTION = new SpoofaxPostTargetType("spoofax_production", BuildTargetKind.PRODUCTION);
    //public static final SpoofaxPostTargetType TESTS = new SpoofaxPostTargetType("spoofax_tests", BuildTargetKind.TEST);

//    private final BuildTargetKind kind;
//
//    public BuildTargetKind kind() { return this.kind; }
//
//    /**
//     * Initializes a new instance of the {@link SpoofaxPostTargetType} class.
//     * @param typeId The type ID of the build target.
//     */
//    protected SpoofaxPostTargetType(String typeId, BuildTargetKind kind) {
//        super(typeId);
//        this.kind = kind;
//    }
//
//    @NotNull
//    @Override
//    public List<SpoofaxPostTarget> computeAllTargets(@NotNull JpsModel model) {
//        List<SpoofaxPostTarget> targets = new ArrayList<>();
//        for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
//            targets.add(new SpoofaxPostTarget(module, this));
//        }
//        return targets;
//    }
//
//    @NotNull
//    @Override
//    public BuildTargetLoader<SpoofaxPostTarget> createLoader(@NotNull JpsModel model) {
//        return new BuildTargetLoader<SpoofaxPostTarget>() {
//            @Nullable
//            @Override
//            public SpoofaxPostTarget createTarget(@NotNull String targetId) {
//                for (JpsTypedModule<JpsDummyElement> module : model.getProject().getModules(JpsSpoofaxModuleType.INSTANCE)) {
//                    if (module.getName().equals(targetId)) {
//                        return new SpoofaxPostTarget(module, SpoofaxPostTargetType.this);
//                    }
//                }
//                return null;
//            }
//        };
//    }
}
