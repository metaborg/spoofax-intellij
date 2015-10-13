package org.metaborg.spoofax.intellij.idea;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.project.IProject;
import org.metaborg.spoofax.intellij.idea.model.IntelliJProject;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.intellij.resources.IntelliJResourceService;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Project service for IntelliJ.
 */
public final class IntelliJProjectService implements IIntelliJProjectService {

    @InjectLogger
    private Logger logger;
    @NotNull
    private final IIntelliJResourceService resourceService;
    @NotNull
    private final Map<Module, IntelliJProject> modules = new HashMap<>();

    @Inject
    private IntelliJProjectService(
            @NotNull final IIntelliJResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void open(@NotNull final IntelliJProject project) {
        this.modules.put(project.getModule(), project);
    }

    public void close(@NotNull final Module module) {
        this.modules.remove(module);
    }

    @Nullable
    @Override
    public IntelliJProject get(@NotNull final Module module) {
        return this.modules.get(module);
    }

    @Nullable
    @Override
    public IntelliJProject get(@NotNull final FileObject resource) {
        final VirtualFile file = this.resourceService.unresolve(resource);
        if (file == null)
            return null;

        final Module module = getModule(file);
        if (module == null)
            return null;
        return get(module);
    }

    /**
     * Gets the {@link Module} for a given {@link VirtualFile}.
     *
     * @param file The file to find.
     * @return The {@link Module} of the file.
     */
    @Nullable
    private Module getModule(@NotNull final VirtualFile file) {
        Set<Module> candidates = new HashSet<>();
        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            final Module module = ModuleUtil.findModuleForFile(file, project);
            if (module != null)
                candidates.add(module);
        }
        if (candidates.size() > 1) {
            logger.error("File {} found in multiple modules. Picking a random one. These modules: {}", file, candidates);
        }
        if (candidates.size() != 0) {
            return candidates.iterator().next();
        }
        return null;
    }
}
