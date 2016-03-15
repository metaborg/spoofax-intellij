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

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;
import com.google.inject.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.projectRoots.impl.*;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.io.*;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.vfs.impl.jar.*;
import com.intellij.util.*;
import org.jdom.*;
import org.jetbrains.annotations.*;
import org.jetbrains.jps.incremental.java.*;
import org.metaborg.intellij.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.log.*;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The Metaborg SDK type.
 */
public final class MetaborgSdkType extends JavaDependentSdkType implements JavaSdkType {

    private static final String NAME = "Metaborg SDK";

    private IIconManager iconManager;
    private LibraryService libraryService;
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
    private void inject(final LibraryService libraryService, final IIconManager iconManager) {
        this.libraryService = libraryService;
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
        return this.libraryService.getPluginLibPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValidSdkHome(final String path) {

        final List<VirtualFile> sdkFiles = getSdkJars(path);

        if (sdkFiles.isEmpty()) {
            // Apparently the SDK has no files, so any home is valid.
            return true;
        }

        // We simply test whether the first file exists.
        return sdkFiles.get(0) != null && sdkFiles.get(0).exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setupSdkPaths(final Sdk metaborgSdk, final SdkModel sdkModel) {

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
            final int choice = Messages
                    .showChooseDialog("Select the JDK to use with Metaborg.", "Select JDK",
                            ArrayUtil.toStringArray(jdkCandidates), jdkName, Messages.getQuestionIcon());

            if (choice == -1) {
                // User cancelled.
                return false;
            }

            jdkName = jdkCandidates.get(choice);
        }

        @Nullable final Sdk jdk = sdkModel.findSdk(jdkName);
        assert jdk != null;

        final SdkModificator sdkModificator = metaborgSdk.getSdkModificator();
        addJdkPaths(sdkModificator, jdk);
        addMetaborgSdkPaths(sdkModificator, metaborgSdk.getHomePath());
        sdkModificator.setSdkAdditionalData(new MetaborgSdkAdditionalData(metaborgSdk, jdk));
        sdkModificator.setVersionString(jdk.getVersionString());
        sdkModificator.commitChanges();

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
    @Nullable
    public String getBinPath(final Sdk sdk) {
        @Nullable final Sdk jdk = getJdk(sdk);
        if (jdk == null) return null;
        return ((JavaSdk)jdk.getSdkType()).getBinPath(jdk);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getToolsPath(final Sdk sdk) {
        @Nullable final Sdk jdk = getJdk(sdk);
        if (jdk == null || jdk.getVersionString() == null) return null;
        return ((JavaSdk)jdk.getSdkType()).getToolsPath(jdk);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getVMExecutablePath(final Sdk sdk) {
        @Nullable final Sdk jdk = getJdk(sdk);
        if (jdk == null) return null;
        return ((JavaSdk)jdk.getSdkType()).getVMExecutablePath(jdk);
    }

    /**
     * Gets the JDK.
     *
     * @param sdk The SDK.
     * @return The JDK; or <code>null</code>.
     */
    @Nullable
    public static Sdk getJdk(final Sdk sdk) {
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
    public static MetaborgSdkAdditionalData getMetaborgSdkAdditionalData(final Sdk sdk) {
        @Nullable final SdkAdditionalData data = sdk.getSdkAdditionalData();
        if (data instanceof MetaborgSdkAdditionalData) {
            return (MetaborgSdkAdditionalData)data;
        } else {
            return null;
        }
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
     * @param sdk The SDK; or <code>null</code>.
     * @return The minimum required JDK version; or <code>null</code>.
     */
    @Nullable
    public static JavaSdkVersion getMinimumJdkVersion(@Nullable final Sdk sdk) {
        // TODO: Determine the minimum JDK version based on the SDK.
        return JavaSdkVersion.JDK_1_7;
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
        for (final VirtualFile file : getSdkJars(sdkHomePath)) {
            sdkModificator.addRoot(file, OrderRootType.CLASSES);
        }
    }

    /**
     * Returns a list of SDK files.
     *
     * @param sdkHomePath The SDK home path.
     * @return A list of SDK jars. A file can be <code>null</code> if it doesn't exist.
     * Otherwise, a file's {@link VirtualFile#exists()} may return <code>false</code>.
     */
    private List<VirtualFile> getSdkJars(final String sdkHomePath) {

        // Specify the Metaborg SDK dependencies in this file.
        final URL url = Resources.getResource(SpoofaxIdeaPlugin.class, "/sdk_libraries.txt");

        // The JARs mentioned must be in the plugin's lib folder (plugins/org.metaborg.intellij/lib)
        // at runtime. To get them there, the JAR must either be a dependency of this plugin (as plugin
        // dependencies are automatically copied to the lib folder), or it must be copied or
        // downloaded to the lib folder at runtime (e.g. from a Maven repository). The LibraryService
        // can help you download stuff to the lib folder.

        final String text;
        try {
            text = Resources.toString(url, Charsets.UTF_8);
        } catch (final IOException e) {
            throw LoggerUtils.exception(this.logger, UnhandledException.class,
                    "Cannot get resource content of resource: {}", e, url);
        }

        final String[] filenames = text.split("\\r?\\n");

        final List<VirtualFile> files = new ArrayList<>(filenames.length);
        for (final String filename : filenames) {
            final File file = new File(sdkHomePath, filename);

            @Nullable final VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByIoFile(file);
            if (virtualFile == null) {
                files.add(null);
                continue;
            }

            @Nullable final VirtualFile jarFile = JarFileSystemImpl.getInstance().getJarRootForLocalFile(virtualFile);
            if (jarFile == null) {
                files.add(null);
                continue;
            }

            files.add(jarFile);
        }

        return files;
    }

}
