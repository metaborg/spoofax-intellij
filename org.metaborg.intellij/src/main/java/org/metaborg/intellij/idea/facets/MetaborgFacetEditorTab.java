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

import com.google.common.collect.*;
import com.google.inject.*;
import com.intellij.facet.*;
import com.intellij.facet.frameworks.*;
import com.intellij.facet.frameworks.beans.*;
import com.intellij.facet.ui.*;
import com.intellij.facet.ui.libraries.*;
import com.intellij.ide.util.frameworkSupport.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.command.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.*;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.vfs.*;
import com.intellij.util.download.*;
import org.apache.commons.vfs2.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.config.*;
import org.metaborg.core.language.*;
import org.metaborg.core.messages.*;
import org.metaborg.core.source.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.intellij.resources.*;
import org.metaborg.spoofax.meta.core.config.*;
import org.metaborg.spoofax.meta.core.project.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.*;

public class MetaborgFacetEditorTab extends FacetEditorTab {

    private JPanel mainPanel;
    private LibraryService libraryService;
    private IProjectConfigBuilder configBuilder;
    private IProjectConfigWriter configWriter;
    private IProjectConfigService configService;
    private IIdeaProjectService projectService;
    private IIdeaProjectFactory projectFactory;
    private ISourceTextService sourceTextService;
    private IIntelliJResourceService resourceService;
    private final FacetEditorContext editorContext;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgFacetEditorTab(final FacetEditorContext editorContext,
                                  final FacetValidatorsManager validatorsManager) {
        SpoofaxIdeaPlugin.injector().injectMembers(this);

        this.editorContext = editorContext;
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final LibraryService libraryService,
                        final IProjectConfigBuilder configBuilder,
                        final IProjectConfigWriter configWriter,
                        final IProjectConfigService configService,
                        final IIdeaProjectService projectService,
                        final IIdeaProjectFactory projectFactory,
                        final ISourceTextService sourceTextService,
                        final IIntelliJResourceService resourceService) {
        this.libraryService = libraryService;
        this.configBuilder = configBuilder;
        this.configWriter = configWriter;
        this.configService = configService;
        this.projectService = projectService;
        this.projectFactory = projectFactory;
        this.sourceTextService = sourceTextService;
        this.resourceService = resourceService;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Metaborg Facet Editor";
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void onFacetInitialized(@NotNull final Facet facet) {
        if (!this.editorContext.isNewFacet())
            return;

        if (facet instanceof MetaborgFacet) {
            final ModifiableRootModel model = ModuleRootManager.getInstance(facet.getModule()).getModifiableModel();
            ((MetaborgFacet)facet).applyFacet(model);
            model.dispose();
        }
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() {

    }

    @NotNull
    @Override
    public JComponent createComponent() {
        return this.mainPanel;
    }

    @Override
    public void disposeUIResources() {

    }
}
