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

package org.metaborg.intellij.idea.gui.modulesettings;

import com.google.inject.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ui.configuration.*;
import org.jetbrains.jps.model.java.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.projects.*;

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
    @Inject
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
        return new ModuleConfigurationEditor[]{
                new CommonContentEntriesEditor(
                        module.getName(),
                        state,
                        JavaSourceRootType.SOURCE,
                        JavaSourceRootType.TEST_SOURCE
                ),
                new ClasspathEditor(state),
                // Add more editors here.
        };
    }
}