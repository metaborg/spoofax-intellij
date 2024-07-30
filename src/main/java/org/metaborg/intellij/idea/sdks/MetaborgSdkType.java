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

package org.metaborg.intellij.idea.sdks;


import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.projectRoots.impl.JavaDependentSdkType;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.impl.jar.JarFileSystemImpl;
import com.intellij.util.ArrayUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.SpoofaxPlugin;
import org.metaborg.intellij.idea.graphics.IIconManager;
import org.metaborg.intellij.idea.utils.SimpleConfigUtil;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.intellij.logging.LoggerUtils2;
import org.metaborg.intellij.resources.LibraryService;
import org.metaborg.util.log.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The Metaborg SDK type.
 */
public final class MetaborgSdkType extends JavaDependentSdkType implements JavaSdkType {

    private static final String NAME = "Metaborg SDK";

    private IIconManager iconManager;
    private LibraryService libraryService;
    private SimpleConfigUtil configUtil;
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

    @jakarta.inject.Inject
    @SuppressWarnings("unused")
    private void inject(final LibraryService libraryService, final IIconManager iconManager, final SimpleConfigUtil configUtil) {
        this.libraryService = libraryService;
        this.iconManager = iconManager;
        this.configUtil = configUtil;
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
    public String getVersionString(final Sdk sdk) {
        // The returned version string must be the JDK's version string,
        // as this is used to determine the build target Java version.
        // Anything else and you'll get errors about having the wrong
        // build target version.
        @Nullable final Sdk jdk = getJdk(sdk);
        return jdk != null ? jdk.getVersionString() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String suggestSdkName(final String currentSdkName, final String sdkHome) {
        return "Metaborg SDK";
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
        final String path = SpoofaxPlugin.INSTANCE.getLibPath().getAbsolutePath();
        this.logger.debug("Suggested Metaborg SDK home path: {}", path);
        return path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidSdkHome(final String path) {
        if (StringUtil.isEmpty(path))
            return false;

        final List<Pair<String, VirtualFile>> sdkFiles = getSdkJars(path);

        if (sdkFiles.isEmpty()) {
            // Apparently the SDK has no files, so any home is valid.
            return true;
        }

        // We simply test whether the first file exists.
        return sdkFiles.get(0).getValue() != null && sdkFiles.get(0).getValue().exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setupSdkPaths(final Sdk metaborgSdk, final SdkModel sdkModel) {

        final SdkModificator sdkModificator = metaborgSdk.getSdkModificator();
        boolean success = setupSdkPaths(metaborgSdk, sdkModificator, sdkModel);
        if (success && sdkModificator.getSdkAdditionalData() == null) {

            @Nullable final JavaSdkVersion minimumJdkVersion = getMinimumJdkVersion(metaborgSdk);
            final List<String> jdkCandidates = new ArrayList<>();
            for (final Sdk sdk : sdkModel.getSdks()) {
                if (isAcceptableJdk(sdk, minimumJdkVersion)) {
                    jdkCandidates.add(sdk.getName());
                }
            }

            if (jdkCandidates.isEmpty()) {
                Messages.showErrorDialog("No JDK found" +
                        (minimumJdkVersion != null ? " of at least version " + minimumJdkVersion.getDescription() : "")
                        + ". Please configure one.", "JDK Not Found");
                return false;
            }

            String jdkName = jdkCandidates.get(0);

            if (jdkCandidates.size() > 1) {
                @SuppressWarnings("deprecation") final int choice
                        = Messages.showChooseDialog("Select the JDK to use with Metaborg.", "Select JDK",
                                ArrayUtil.toStringArray(jdkCandidates), jdkName, Messages.getQuestionIcon());

                if (choice == -1) {
                    // User cancelled.
                    success = false;
                }

                jdkName = jdkCandidates.get(choice);
            }

            @Nullable final Sdk jdk = sdkModel.findSdk(jdkName);
            assert jdk != null;
            setJdk(metaborgSdk, sdkModificator, jdk);
        }
        sdkModificator.commitChanges();

        return success;
    }

    private boolean setupSdkPaths(Sdk metaborgSdk, SdkModificator sdkModificator, SdkModel model) {
        return true;
    }

    private void setJdk(Sdk metaborgSdk, SdkModificator sdkModificator, Sdk jdk) {
        addJdkPaths(sdkModificator, jdk);
        addMetaborgSdkPaths(sdkModificator, metaborgSdk.getHomePath());
        sdkModificator.setSdkAdditionalData(new MetaborgSdkAdditionalData(metaborgSdk, jdk));
        sdkModificator.setVersionString(jdk.getVersionString());
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
    @Nullable
    public String getBinPath(final Sdk sdk) {
        @Nullable final Sdk jdk = getJdk(sdk);
        if (jdk == null) return null;
        return JavaSdk.getInstance().getBinPath(jdk);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getToolsPath(final Sdk sdk) {
        @Nullable final Sdk jdk = getJdk(sdk);
        if (jdk == null || jdk.getVersionString() == null) return null;
        return JavaSdk.getInstance().getToolsPath(jdk);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getVMExecutablePath(final Sdk sdk) {
        @Nullable final Sdk jdk = getJdk(sdk);
        if (jdk == null) return null;
        return JavaSdk.getInstance().getVMExecutablePath(jdk);
    }

    /**
     * Gets the JDK.
     *
     * @param sdk The SDK.
     * @return The JDK; or <code>null</code>.
     */
    @Nullable
    private static Sdk getJdk(final Sdk sdk) {
        @Nullable final MetaborgSdkAdditionalData data = getMetaborgSdkAdditionalData(sdk);
        if (data == null) return null;
        return data.getJdk();
    }

    /**
     * Gets the {@link MetaborgSdkAdditionalData} of the specified SDK.
     *
     * @param sdk The SDK.
     * @return The Metaborg data; or <code>null</code>.
     */
    @Nullable
    private static MetaborgSdkAdditionalData getMetaborgSdkAdditionalData(final Sdk sdk) {
        @Nullable final SdkAdditionalData data = sdk.getSdkAdditionalData();
        if (data instanceof MetaborgSdkAdditionalData) {
            return (MetaborgSdkAdditionalData)data;
        } else {
            return null;
        }
    }

    /**
     * Determines whether the specified JDK is acceptable.
     *
     * @param jdk The JDK to test.
     * @param minimumJdkVersion The minimum JDK version.
     * @return <code>true</code> when the JDK is acceptable;
     * otherwise, <code>false</code>.
     */
    private static boolean isAcceptableJdk(@Nullable final Sdk jdk,
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
    /* package private */ static boolean isAcceptableJdk(@Nullable final Sdk jdk,
                                   @Nullable final Sdk sdk) {
        return isAcceptableJdk(jdk, getMinimumJdkVersion(sdk));
    }

    /**
     * Gets the minimum required JDK version.
     *
     * @param sdk The SDK; or <code>null</code>.
     * @return The minimum required JDK version; or <code>null</code>.
     */
    @Nullable
    private static JavaSdkVersion getMinimumJdkVersion(@Nullable final Sdk sdk) {
        // TODO: Determine the minimum JDK version based on the SDK.
        return JavaSdkVersion.JDK_1_8;
    }

    /**
     * Adds the JDK paths.
     *
     * @param sdkModificator The SDK modificator.
     * @param jdk The JDK.
     */
    private void addJdkPaths(final SdkModificator sdkModificator,
                             final Sdk jdk) {

        // This adds the JDK jars to the SDK. Without them, you get
        // errors when compiling.
        for (final String url : jdk.getRootProvider().getUrls(OrderRootType.CLASSES)) {
            @Nullable final VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(url);
            if (virtualFile != null) {
                sdkModificator.addRoot(virtualFile, OrderRootType.CLASSES);
            }
        }

    }

    /**
     * Adds the Metaborg SDK paths.
     *
     * @param sdkModificator The SDK modificator.
     * @param sdkHomePath The SDK home path.
     */
    private void addMetaborgSdkPaths(final SdkModificator sdkModificator,
                                     @Nullable final String sdkHomePath) {
        if (sdkHomePath == null) {
            // Anything else we need to do?
            return;
        }

        // The added SDK files must be in the jar:// file system.
        // Adding normal file:// files works when creating the SDK,
        // but they are lost when the SDK is reloaded (e.g. after restart).
        for (final Pair<String, VirtualFile> pair : getSdkJars(sdkHomePath)) {
            String filename = pair.getKey();
            @Nullable VirtualFile file = pair.getValue();
            if (file == null) {
                this.logger.error("SDK file not found: {}", filename);
            } else {
                if (!file.exists()) {
                    this.logger.warn("SDK file may not exist: {}", filename);
                }
                sdkModificator.addRoot(file, OrderRootType.CLASSES);
            }
        }
    }

    /**
     * Returns a list of SDK files.
     *
     * @param sdkHomePath The SDK home path.
     * @return A list of pairs of SDK jar filenames and files. A file can be <code>null</code> if it doesn't exist.
     * Otherwise, a file's {@link VirtualFile#exists()} may return <code>false</code>.
     */
    private List<Pair<String, VirtualFile>> getSdkJars(final String sdkHomePath) {

        final List<String> filenames = configUtil.readResource("/sdk_libraries.txt");
        final List<Pair<String, VirtualFile>> files = new ArrayList<>(filenames.size());
        for (final String filename : filenames) {
            final String finalFilename = filename.replace("${version}", SpoofaxPlugin.INSTANCE.getVersion());

            final File file = new File(sdkHomePath, finalFilename);

            @Nullable final VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);

            @Nullable final VirtualFile jarFile = virtualFile != null ? JarFileSystemImpl.getInstance().getJarRootForLocalFile(virtualFile) : null;

            files.add(Pair.of(filename, jarFile));
        }

        return files;
    }

}
