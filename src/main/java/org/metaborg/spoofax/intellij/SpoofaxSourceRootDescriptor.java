package org.metaborg.spoofax.intellij;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildRootDescriptor;
import org.jetbrains.jps.builders.BuildTarget;

import java.io.File;

/**
 * Describes a source root for the Spoofax build target.
 */
public final class SpoofaxSourceRootDescriptor extends BuildRootDescriptor {

    @NotNull
    private final File root;
    @NotNull
    private final BuildTarget<?> target;

    /**
     * Initializes a new instance of the {@link SpoofaxSourceRootDescriptor} class.
     *
     * @param root   The directory of the source root.
     * @param target The build target to which the source root belongs.
     */
    public SpoofaxSourceRootDescriptor(@NotNull final File root, @NotNull final BuildTarget<?> target) {
        this.root = root;
        this.target = target;
    }

    @Override
    @NotNull
    public final String getRootId() {
        return this.root.getAbsolutePath();
    }

    @Override
    @NotNull
    public final File getRootFile() {
        return this.root;
    }

    @Override
    @NotNull
    public final BuildTarget<?> getTarget() {
        return this.target;
    }

}