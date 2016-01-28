/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.idea.project;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ui.configuration.ClasspathEditor;
import com.intellij.openapi.roots.ui.configuration.CommonContentEntriesEditor;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationEditorProvider;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.metaborg.spoofax.intellij.factories.ILanguageImplEditorFactory;
import org.metaborg.spoofax.intellij.idea.SpoofaxIdeaPlugin;

/**
 * Provides editors for the module's settings in the <em>Project Structure</em> dialog.
 */
@Singleton
public final class SpoofaxModuleConfigurationEditorProvider implements ModuleConfigurationEditorProvider {

    @NotNull
    private SpoofaxModuleType spoofaxModuleType;
    @NotNull
    private ILanguageImplEditorFactory languageImplEditorFactory;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxModuleConfigurationEditorProvider() {
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    private void inject(
            @NotNull final SpoofaxModuleType spoofaxModuleType,
            @NotNull final ILanguageImplEditorFactory languageImplEditorFactory) {
        this.spoofaxModuleType = spoofaxModuleType;
        this.languageImplEditorFactory = languageImplEditorFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ModuleConfigurationEditor[] createEditors(@NotNull final ModuleConfigurationState state) {
        final Module module = state.getRootModel().getModule();
        final ModuleType moduleType = ModuleType.get(module);
        if (moduleType != this.spoofaxModuleType) {
            return ModuleConfigurationEditor.EMPTY;
        }
        return new ModuleConfigurationEditor[]{
                new CommonContentEntriesEditor(module.getName(),
                                               state,
                                               JavaSourceRootType.SOURCE,
                                               JavaSourceRootType.TEST_SOURCE),
                new ClasspathEditor(state),
                this.languageImplEditorFactory.create(state),
        };
    }
}
