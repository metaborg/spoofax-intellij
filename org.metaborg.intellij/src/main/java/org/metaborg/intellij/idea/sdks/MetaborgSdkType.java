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

package org.metaborg.intellij.idea.sdks;

import com.google.inject.*;
import com.intellij.openapi.application.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.projectRoots.impl.*;
import org.jdom.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.facets.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.swing.*;

/**
 * The Metaborg SDK type.
 */
public final class MetaborgSdkType extends JavaDependentSdkType implements JavaSdkType {

    private static final String NAME = "Metaborg SDK";
    private static final String METABORG_CORE_JAR = "org.metaborg.core-2.0.0-SNAPSHOT.jar";

    private IIconManager iconManager;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgSdkType() {
        super(MetaborgSdkType.NAME);
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final IIconManager iconManager) {
        this.iconManager = iconManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPresentableName() {
        return MetaborgSdkType.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getVersionString(final String sdkHome) {
        // TODO: Add version string to Metaborg Core?
        return "2.0.0-SNAPSHOT";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String suggestSdkName(final String currentSdkName, final String sdkHome) {
        return getPresentableName() + " " + getVersionString(sdkHome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getIcon() {
        return this.iconManager.getSdkIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getIconForAddAction() {
        return this.iconManager.getSdkIconForAddAction();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String suggestHomePath() {
        return PathManager.getJarPathForClass(MetaborgSdkType.class);
    }

    @Override
    public boolean isValidSdkHome(final String path) {
        // TODO: Test sdk path.
        return true;
    }




    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public AdditionalDataConfigurable createAdditionalDataConfigurable(
            final SdkModel sdkModel, final SdkModificator sdkModificator) {
        // No additional configuration data needed.
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public SdkAdditionalData loadAdditionalData(@NotNull final Sdk currentSdk, final Element additional) {
        return MetaborgSdkAdditionalData.load(additional, currentSdk);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public SdkAdditionalData loadAdditionalData(final Element additional) {
        throw new IllegalStateException("Use the other overload of loadAdditionalData().");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveAdditionalData(@NotNull final SdkAdditionalData additionalData, @NotNull final Element additional) {
        if (!(additionalData instanceof MetaborgSdkAdditionalData)) return;

        final MetaborgSdkAdditionalData data = (MetaborgSdkAdditionalData)additionalData;
        data.save(additional);
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public String getBinPath(final Sdk sdk) {
        @Nullable final Sdk jdk = getBestJdk();
        assert jdk != null;
        return ((JavaSdk)jdk.getSdkType()).getBinPath(sdk);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolsPath(final Sdk sdk) {
        @Nullable final Sdk jdk = getBestJdk();
        assert jdk != null;
        return ((JavaSdk)jdk.getSdkType()).getToolsPath(sdk);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVMExecutablePath(final Sdk sdk) {
        @Nullable final Sdk jdk = getBestJdk();
        assert jdk != null;
        return ((JavaSdk)jdk.getSdkType()).getVMExecutablePath(sdk);
    }

    /**
     * Gets the best fitting JDK.
     *
     * @return The JDK; or <code>null</code> if not found.
     */
    @Nullable
    public static Sdk getBestJdk() {
        @Nullable Sdk bestJdk = null;
        @Nullable JavaSdkVersion bestJdkVersion = null;

        for(@Nullable final Sdk jdk : ProjectJdkTable.getInstance().getAllJdks()) {

            if (isAcceptableJdk(jdk, bestJdkVersion)) {
                bestJdk = jdk;
                bestJdkVersion = ((JavaSdk) jdk.getSdkType()).getVersion(bestJdk);
                assert bestJdkVersion != null;
            }

        }
        return bestJdk;
    }

    /**
     * Determines whether the specified JDK is acceptable.
     *
     * @param jdk The JDK to test.
     * @param minimumJdkVersion The minimum JDK version.
     * @return <code>true</code> when the JDK is acceptable;
     * otherwise, <code>false</code>.
     */
    public static boolean isAcceptableJdk(@Nullable final Sdk jdk,
                                          @Nullable final JavaSdkVersion minimumJdkVersion) {
        if(jdk == null || !(jdk.getSdkType() instanceof JavaSdk))
            return false;

        final JavaSdk jdkType = ((JavaSdk) jdk.getSdkType());

        @Nullable final JavaSdkVersion jdkVersion = jdkType.getVersion(jdk);
        assert jdkVersion != null;

        return minimumJdkVersion == null || jdkVersion.isAtLeast(minimumJdkVersion);
    }

    /**
     * Determines whether the specified JDK is acceptable.
     *
     * @param jdk The JDK to test.
     * @param sdk The Metaborg SDK.
     * @return <code>true</code> when the JDK is acceptable;
     * otherwise, <code>false</code>.
     */
    public static boolean isAcceptableJdk(@Nullable final Sdk jdk,
                                          @Nullable final Sdk sdk) {
        return isAcceptableJdk(jdk, getMinimumJdkVersion(sdk));
    }

    /**
     * Gets the minimum required JDK version.
     *
     * @param sdk The SDK.
     * @return The minimum required JDK version.
     */
    public static JavaSdkVersion getMinimumJdkVersion(final Sdk sdk) {
        // TODO: Determine the minimum JDK version based on the SDK.
        return JavaSdkVersion.JDK_1_7;
    }
}
