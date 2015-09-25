package org.metaborg.spoofax.intellij;

import com.google.common.base.Preconditions;
import org.jetbrains.jps.builders.BuildRootDescriptor;
import org.jetbrains.jps.builders.BuildTarget;

import java.io.File;

/**
 * Describes a source root for the Spoofax build target.
 */
public class SpoofaxSourceRootDescriptor extends BuildRootDescriptor {

    private final File root;
    private final BuildTarget<?> target;

    /**
     * Initializes a new instance of the {@link SpoofaxSourceRootDescriptor} class.
     * @param root The directory of the source root.
     * @param target The build target to which the source root belongs.
     */
    public SpoofaxSourceRootDescriptor(File root, BuildTarget<?> target)
    {
        Preconditions.checkNotNull(root);
        Preconditions.checkNotNull(target);

        this.root = root;
        this.target = target;
    }

    @Override
    public String getRootId() {
        return this.root.getAbsolutePath();
    }

    @Override
    public File getRootFile() {
        return this.root;
    }

    @Override
    public BuildTarget<?> getTarget() {
        return this.target;
    }

}