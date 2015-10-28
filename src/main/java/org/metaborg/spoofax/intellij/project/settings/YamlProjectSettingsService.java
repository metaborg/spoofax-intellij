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

package org.metaborg.spoofax.intellij.project.settings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.settings.IProjectSettings;
import org.metaborg.core.project.settings.YAMLProjectSettingsSerializer;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.core.StringFormatter;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;

/**
 * A project settings service that stores the settings in a YAML file.
 */
@Singleton
public final class YamlProjectSettingsService implements IProjectSettingsService2 {

    /**
     * Path to the project settings file, relative to the project's root.
     */
    @NotNull
    private static final String SETTINGS_FILE = "spoofax-project.yaml";
    @NotNull
    private static final String ALT_SETTINGS_FILE = "src-gen/metaborg.generated.yaml";
    /**
     * Path to the default settings file, relative to the resources root.
     */
    @NotNull
    private static final String DEFAULT_SETTINGS_FILE = "defaultsettings.yaml";
    @NotNull
    private final IResourceService resourceService;
    @InjectLogger
    private Logger logger;

    @Inject
    private YamlProjectSettingsService(@NotNull final IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public final IProjectSettings getDefault() {
        URL resource = this.getClass().getClassLoader().getResource(DEFAULT_SETTINGS_FILE);
        assert resource != null;
        final String url = resource.toString();
        final FileObject defaultSettings = this.resourceService.resolve(url);

        try {
            return YAMLProjectSettingsSerializer.read(defaultSettings);
        } catch (IOException e) {
            throw new MetaborgRuntimeException(StringFormatter.format(
                    "Reading default settings file failed unexpectedly: {}",
                    defaultSettings), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public IProjectSettings get(@NotNull final IProject project) {
        final FileObject location = project.location();
        try {
            final FileObject settingsFile = getSettingsFile(location);
            if (settingsFile == null) {
                return getDefault();
            }
            return YAMLProjectSettingsSerializer.read(settingsFile);
        } catch (IOException e) {
            logger.warn(String.format("Reading settings file failed unexpectedly: %s/%s", location, SETTINGS_FILE), e);
            return getDefault();
        }
    }

    @Nullable
    private FileObject getSettingsFile(@NotNull FileObject projectLocation) throws FileSystemException {
        FileObject settingsFile;
        settingsFile = projectLocation.resolveFile(SETTINGS_FILE);
        if (settingsFile.exists()) return settingsFile;
        settingsFile = projectLocation.resolveFile(ALT_SETTINGS_FILE);
        if (settingsFile.exists()) return settingsFile;
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public void set(@NotNull final IProject project, @NotNull final IProjectSettings settings) {
        final FileObject location = project.location();
        try {
            final FileObject settingsFile = location.resolveFile(SETTINGS_FILE);
            YAMLProjectSettingsSerializer.write(settingsFile, settings);
        } catch (IOException e) {
            logger.warn(String.format("Writing settings file failed unexpectedly: %s/%s", location, SETTINGS_FILE), e);
        }
    }
}
