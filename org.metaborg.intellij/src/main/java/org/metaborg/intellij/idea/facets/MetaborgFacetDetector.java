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

package org.metaborg.intellij.idea.facets;

import com.google.inject.*;
import com.intellij.facet.*;
import com.intellij.framework.detection.*;
import com.intellij.openapi.fileTypes.*;
import com.intellij.patterns.*;
import com.intellij.util.indexing.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

/**
 * Detects the Metaborg facet.
 *
 * A module gets the Metaborg facet if it has a <code>metaborg.yaml</code> file in the root,
 * but is not a {@link MetaborgModuleType}.
 */
@Deprecated
public class MetaborgFacetDetector extends FacetBasedFrameworkDetector<MetaborgFacet, IdeaMetaborgModuleFacetConfig> {

    private MetaborgFacetType facetType;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgFacetDetector() {
        super(MetaborgFacetType.NAME);

        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final MetaborgFacetType facetType) {
        this.facetType = facetType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FacetType<MetaborgFacet, IdeaMetaborgModuleFacetConfig> getFacetType() {
        return this.facetType;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public FileType getFileType() {
        return FileTypeManager.getInstance().getFileTypeByExtension("yaml");
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public ElementPattern<FileContent> createSuitableFilePattern() {
        // See: https://github.com/troger/nuxeo-intellij/blob/c651cda7e5066a383a76e1f65dcc33ce5fe27fcf/src/org/nuxeo/intellij/facet/NuxeoFrameworkDetector.java

        // FIXME: Can't implement ModuleType dependent.
        return null;
    }
}
