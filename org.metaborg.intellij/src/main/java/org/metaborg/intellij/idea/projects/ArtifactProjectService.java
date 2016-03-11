/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.intellij.idea.projects;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.IProjectService;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.resources.FileNameUtils;
import org.metaborg.util.log.ILogger;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

// TODO: Move to Spoofax core?

/**
 * Project service for language artifacts.
 */
public final class ArtifactProjectService implements IProjectService {

    private final IArtifactProjectFactory artifactProjectFactory;
    private final Map<FileName, ArtifactProject> projects = new HashMap<>();
    @InjectLogger
    private ILogger logger;

    @Inject
    public ArtifactProjectService(final IArtifactProjectFactory artifactProjectFactory) {
        this.artifactProjectFactory = artifactProjectFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public IProject get(final FileObject resource) {
        Preconditions.checkNotNull(resource);

        @Nullable final FileObject artifactRoot = getArtifactRoot(resource);
        if (artifactRoot == null)
            return null;

        final FileName artifactName = artifactRoot.getName();

        @Nullable ArtifactProject project = this.projects.get(artifactName);
        if (project == null) {
            project = this.artifactProjectFactory.create(artifactRoot, null);
            this.projects.put(artifactName, project);
        }
        return project;
    }

    /**
     * Determines the language artifact root of the specified file.
     *
     * @param file The file.
     * @return The language artifact root; or <code>null</code> when there is none.
     */
    @Nullable
    private FileObject getArtifactRoot(final FileObject file) {
        @Nullable FileObject current = getRoot(file);
        while (current != null && !isArtifactRoot(current.getName())) {
            current = getParentRoot(current);
        }
        return current;
    }

    /**
     * Gets the root file of the specified layer.
     *
     * @param layer The layer; or <code>null</code>.
     * @return The root file; or <code>null</code>.
     */
    @Nullable
    private FileObject getRoot(@Nullable final FileObject layer) {
        if (layer == null)
            return null;
        try {
            return layer.getFileSystem().getRoot();
        } catch (final FileSystemException e) {
            this.logger.error("Ignored exception.", e);
            return null;
        }
    }

    /**
     * Determines whether the specified file name points to a language artifact root.
     * <p>
     * For example, <code>zip:file:///dir/archive.spoofax-language!/</code> is a language artifact root.
     *
     * @param fileName The file name to check.
     * @return <code>true</code> when the file is a language artifact root;
     * otherwise, <code>false</code>.
     */
    private boolean isArtifactRoot(final FileName fileName) {
        @Nullable final FileName outerFileName = FileNameUtils.getOuterFileName(fileName);
        if (outerFileName == null)
            return false;
        return "spoofax-language".equals(outerFileName.getExtension());
    }

    /**
     * Gets the parent root of the specified file.
     *
     * @param file The file.
     * @return The parent root; or <code>null</code>.
     */
    @Nullable
    private FileObject getParentRoot(final FileObject file) {
        try {
            return getRoot(file.getFileSystem().getParentLayer());
        } catch (final FileSystemException e) {
            this.logger.error("Ignored exception.", e);
            return null;
        }
    }

}
