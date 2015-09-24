package org.metaborg.spoofax.intellij.jps.targets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProject;

public final class SpoofaxNewPreTargetType extends SpoofaxNewTargetType<SpoofaxNewPreTarget> {

    public static final SpoofaxNewPreTargetType INSTANCE = new SpoofaxNewPreTargetType();   // TODO: Refactor

    public SpoofaxNewPreTargetType() {
        super("spoofax-pre-production");
    }

    @NotNull
    public SpoofaxNewPreTarget createTarget(IProject project, JpsModule module) {
        return new SpoofaxNewPreTarget(this, module);
    }

}
