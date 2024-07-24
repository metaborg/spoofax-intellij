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

package org.metaborg.intellij.idea.gui.modulesettings;


import com.google.inject.Singleton;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ui.configuration.DefaultModuleConfigurationEditorFactory;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationEditorProvider;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.projects.MetaborgModuleType;

/**
 * Provides editors for the module's settings in the <em>Project Structure</em> dialog.
 */
@Singleton
public final class MetaborgModuleConfigurationEditorProvider implements ModuleConfigurationEditorProvider {

    private MetaborgModuleType metaborgModuleType;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgModuleConfigurationEditorProvider() {
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @SuppressWarnings("unused")
    @jakarta.inject.Inject
    private void inject(final MetaborgModuleType metaborgModuleType) {
        this.metaborgModuleType = metaborgModuleType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ModuleConfigurationEditor[] createEditors(final ModuleConfigurationState state) {
        final Module module = state.getRootModel().getModule();
        final ModuleType moduleType = ModuleType.get(module);
        if (moduleType != this.metaborgModuleType) {
            return ModuleConfigurationEditor.EMPTY;
        }

        final DefaultModuleConfigurationEditorFactory editorFactory
                = DefaultModuleConfigurationEditorFactory.getInstance();

        return new ModuleConfigurationEditor[]{
                editorFactory.createModuleContentRootsEditor(state),
                editorFactory.createOutputEditor(state),
                editorFactory.createClasspathEditor(state)
                // Add more editors here.
        };
    }
}