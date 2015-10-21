package org.metaborg.spoofax.intellij.idea.project;

import com.intellij.ide.util.projectWizard.importSources.DetectedProjectRoot;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A detected Spoofax project's root.
 */
public final class SpoofaxProjectRoot extends DetectedProjectRoot {

    /**
     * Initializes a new instance of the {@link SpoofaxProjectRoot} class.
     *
     * @param directory The root directory.
     */
    public SpoofaxProjectRoot(@NotNull final File directory) {
        super(directory);
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public String getRootTypeName() {
        return "Spoofax module";
    }
}
