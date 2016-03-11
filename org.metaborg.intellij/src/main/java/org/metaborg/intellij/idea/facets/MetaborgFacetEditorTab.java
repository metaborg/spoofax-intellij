/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    private void inject() {
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
