package org.metaborg.spoofax.intellij;

import com.google.common.base.Preconditions;
import org.jetbrains.jps.builders.BuildRootDescriptor;
import org.jetbrains.jps.builders.BuildTarget;

import java.io.File;

public class SpoofaxSourceRootDescriptor extends BuildRootDescriptor {

    private final File root;
    private final SpoofaxBuildTarget target;

    public SpoofaxSourceRootDescriptor(File root, SpoofaxBuildTarget target)
    {
        Preconditions.checkNotNull(root);
        Preconditions.checkNotNull(target);

        this.root = root;
        this.target = target;
    }

    @Override
    public String getRootId() {
        return "SpoofaxSourceRootDescriptor";
    }

    @Override
    public File getRootFile() {
        return this.root;
    }

    @Override
    public BuildTarget<?> getTarget() {
        return this.target;
    }

    // https://github.com/kingsleyh/DLanguage/blob/326b0920b44fcf1b72b81c98a1c8814b2879122f/jps-plugin/src/net/masterthought/dlanguage/jps/DSourceRootDescriptor.java
    // https://github.com/pantsbuild/intellij-pants-plugin/blob/6fe84a536b4275358a38487f751fd64d6d5c9163/jps-plugin/com/twitter/intellij/pants/jps/incremental/model/PantsSourceRootDescriptor.java
}