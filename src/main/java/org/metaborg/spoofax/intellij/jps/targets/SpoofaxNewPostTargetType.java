package org.metaborg.spoofax.intellij.jps.targets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.IProject;

public final class SpoofaxNewPostTargetType extends SpoofaxNewTargetType<SpoofaxNewPostTarget> {

    public static final SpoofaxNewPostTargetType INSTANCE = new SpoofaxNewPostTargetType();   // TODO: Refactor

    public SpoofaxNewPostTargetType() {
        super("spoofax-post-production");
    }

    @NotNull
    public SpoofaxNewPostTarget createTarget(IProject project, JpsModule module) {
        return new SpoofaxNewPostTarget(this, module);
    }

}
