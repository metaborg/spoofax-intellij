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


import com.intellij.facet.Facet;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import javax.swing.*;
import java.awt.*;

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

    @jakarta.inject.Inject
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
        if (!this.editorContext.isNewFacet()) {
            return;
        }

        if (facet instanceof MetaborgFacet) {
            final ModifiableRootModel model = ModuleRootManager.getInstance(facet.getModule()).getModifiableModel();
            ((MetaborgFacet) facet).applyFacet(model);
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
