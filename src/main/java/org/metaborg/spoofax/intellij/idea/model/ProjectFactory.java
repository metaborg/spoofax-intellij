package org.metaborg.spoofax.intellij.idea.model;

import com.google.inject.Inject;
import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;

public final class ProjectFactory {

    /**
     * Creates a new instance of the @link{ProjectFactory} class.
     */
    @Inject
    private ProjectFactory() {
    }

    /**
     * Creates a new @link{IntelliJProject}.
     *
     * @param intellijModule The IntelliJ module.
     * @param contentPath    Where the module lives.
     */
    @NotNull
    public final IntelliJProject create(@NotNull final Module intellijModule, @NotNull final FileObject contentPath) {
        return new IntelliJProject(intellijModule, contentPath);
    }
}
