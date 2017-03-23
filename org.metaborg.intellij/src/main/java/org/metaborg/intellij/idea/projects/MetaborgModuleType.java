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

package org.metaborg.intellij.idea.projects;


import com.google.inject.Inject;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.Sdk;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.graphics.IIconManager;
import org.metaborg.intellij.projects.MetaborgModuleConstants;

import javax.swing.*;

/**
 * The type of a Spoofax module.
 */
public final class MetaborgModuleType extends ModuleType<MetaborgModuleBuilder> {

    public static ModuleType getModuleType() {
        return ModuleTypeManager.getInstance().findByID(ID);
    }

    // The module ID. This is displayed to the user when the ModuleType cannot be found.
    public static final String ID = MetaborgModuleConstants.ModuleID;
    private static final String NAME = "Spoofax";
    private static final String DESCRIPTION = "Spoofax Module";

    private IIconManager iconManager;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgModuleType() {
        super(ID);
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final IIconManager iconManager) {
        assert iconManager != null;
        this.iconManager = iconManager;
    }

    /**
     * Creates a module builder.
     *
     * @return The created module builder.
     */
    @Override
    public MetaborgModuleBuilder createModuleBuilder() {
        // TODO: Factory?
        return SpoofaxIdeaPlugin.injector().getInstance(MetaborgModuleBuilder.class);
    }

    /**
     * Gets the name of the module type.
     *
     * @return The name of the module type.
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Gets the description of the module type.
     *
     * @return The description.
     */
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * Gets the big icon for this module type.
     *
     * @return The big icon.
     */
    @Override
    public Icon getBigIcon() {
        return this.iconManager.getDefaultIcon();
    }

    /**
     * Gets the icon for this module type.
     *
     * @param isOpened Whether the module is expanded.
     * @return The icon.
     */
    @Override
    public Icon getNodeIcon(@Deprecated final boolean isOpened) {
        return this.iconManager.getDefaultIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidSdk(final Module module, final Sdk projectSdk) {
        return super.isValidSdk(module, projectSdk);
    }
}