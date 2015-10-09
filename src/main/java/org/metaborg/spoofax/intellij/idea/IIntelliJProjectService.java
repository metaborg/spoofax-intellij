package org.metaborg.spoofax.intellij.idea;

import com.intellij.openapi.module.Module;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;

import javax.annotation.Nullable;

public interface IIntelliJProjectService extends IProjectService {

    /**
     * Retrieves the Spoofax project of a given IntelliJ module.
     *
     * @param module The IntelliJ module.
     * @return The corresponding Spoofax project;
     * or <code>null</code> if no project could be found.
     */
    @Nullable
    IProject get(Module module);

}
