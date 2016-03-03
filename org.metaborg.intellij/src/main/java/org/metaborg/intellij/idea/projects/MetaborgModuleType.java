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


import com.google.inject.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.*;
import com.intellij.openapi.projectRoots.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.graphics.*;

import javax.swing.*;

/**
 * The type of a Spoofax module.
 */
public final class MetaborgModuleType extends ModuleType<MetaborgModuleBuilder> {

    public static ModuleType getModuleType() {
        return ModuleTypeManager.getInstance().findByID(ID);
    }

    // The module ID. This is displayed to the user when the ModuleType cannot be found.
    public static final String ID = "METABORG_MODULE"; // This is also used in plugin.xml.
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