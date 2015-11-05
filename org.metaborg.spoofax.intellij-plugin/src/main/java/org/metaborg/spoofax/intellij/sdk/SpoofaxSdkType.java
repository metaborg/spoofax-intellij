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

package org.metaborg.spoofax.intellij.sdk;

import com.google.inject.Singleton;
import com.intellij.openapi.projectRoots.*;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxIcons;

import javax.swing.*;

/**
 * The Spoofax SDK type.
 */
@Singleton
public final class SpoofaxSdkType extends SdkType {

    private static final String ID = "SpoofaxSDKType";

    public SpoofaxSdkType() {
        super(ID);
    }

    /**
     * Gets a default home path, if any.
     *
     * @return A default SDK home path; or <code>null</code>.
     */
    @Nullable
    @Override
    public String suggestHomePath() {
        // Default implementation.
        return null;
    }

    /**
     * Determines whether the specified path is valid as the SDK home.
     * <p>
     * This method could check, for example, the precense of certain files and directories.
     *
     * @param path The path to check.
     * @return <code>true</code> when the specified path is a valid SDK home;
     * otherwise, <code>false</code>.
     */
    @Override
    public boolean isValidSdkHome(final String path) {
        // Default implementation
        return true;
    }

    @Nullable
    @Override
    public String getVersionString(final String sdkHome) {
        return "1.1.5";
    }

    /**
     * Gets a suggested name for the SDK instance.
     *
     * @param currentSdkName The current SDK name.
     * @param sdkHome        The SDK home.
     * @return The suggested SDK name.
     */
    @Override
    public String suggestSdkName(final String currentSdkName, final String sdkHome) {
        return getVersionString(sdkHome) + " (suggested)";
    }

    @Nullable
    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(
            final SdkModel sdkModel,
            final SdkModificator sdkModificator) {
        // Default implementation.
        return null;
    }

    /**
     * Gets the presentable name of the SDK type.
     *
     * @return The presentable name.
     */
    @Override
    public String getPresentableName() {
        return "Spoofax SDK Type (presentable)";
    }

    /**
     * Gets the icon for the SDK.
     *
     * @return The SDK's icon.
     */
    @Override
    public Icon getIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    @Override
    public Icon getIconForAddAction() {
        return getIcon();
    }

    @Override
    public void saveAdditionalData(final SdkAdditionalData sdkAdditionalData, final Element element) {
        // TODO: Save SDK data in the project.
    }
}
