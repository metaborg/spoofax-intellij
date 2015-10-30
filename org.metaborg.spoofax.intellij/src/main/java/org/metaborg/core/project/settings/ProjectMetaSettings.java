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

package org.metaborg.core.project.settings;

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.LanguageContributionIdentifier;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.spoofax.core.project.settings.Format;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Meta settings for a project.
 */
public final class ProjectMetaSettings extends NewProjectSettings implements IProjectMetaSettings {

    /* package private */ static final SettingKey LOCATION_KEY
            = new SettingKey("location", FileObject.class);
    /* package private */ static final SettingKey PARDONED_LANGUAGES_KEY
            = new SettingKey("pardonedLanguages", new TypeReference<List<String>>(){});
    /* package private */ static final SettingKey FORMAT_KEY
            = new SettingKey("format", Format.class);
    /* package private */ static final SettingKey SDF_ARGS_KEY
            = new SettingKey("sdfArgs", Format.class);
    /* package private */ static final SettingKey STRATEGO_ARGS_KEY
            = new SettingKey("strategoArgs", Format.class);
    /* package private */ static final SettingKey EXTERNAL_DEF_KEY
            = new SettingKey("externalDef", Format.class);
    /* package private */ static final SettingKey EXTERNAL_JAR_KEY
            = new SettingKey("externalJar", Format.class);
    /* package private */ static final SettingKey EXTERNAL_JAR_FLAGS_KEY
            = new SettingKey("externalJarFlags", Format.class);
    /* package private */ static final SettingKey STRATEGO_KEY
            = new SettingKey("strategoName", Format.class);
    /* package private */ static final SettingKey JAVA_NAME_KEY
            = new SettingKey("javaName", Format.class);
    /* package private */ static final SettingKey PACKAGE_NAME_KEY
            = new SettingKey("packageName", Format.class);
    /* package private */ static final SettingKey PACKAGE_PATH_KEY
            = new SettingKey("packagePath", Format.class);

    /**
     * Initializes a new instance of the {@link Settings} class.
     *
     * @param settings The map of settings to use.
     * @param parent The parent settings; or <code>null</code>.
     */
    /* package private */ ProjectMetaSettings(@NotNull final Map<SettingKey, Object> settings, @Nullable final Settings parent) {
        super(settings, parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject location() {
        return getSetting(LOCATION_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> pardonedLanguages() {
        return getSetting(PARDONED_LANGUAGES_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Format format() {
        return getSetting(FORMAT_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> sdfArgs() {
        return getSetting(SDF_ARGS_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> strategoArgs() {
        return getSetting(STRATEGO_ARGS_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String externalDef() {
        return getSetting(EXTERNAL_DEF_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String externalJar() {
        return getSetting(EXTERNAL_JAR_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String externalJarFlags() {
        return getSetting(EXTERNAL_JAR_FLAGS_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String strategoName() {
        return getSetting(STRATEGO_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String javaName() {
        return getSetting(JAVA_NAME_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String packageName() {
        return getSetting(PACKAGE_NAME_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String packagePath() {
        return getSetting(PACKAGE_PATH_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getGeneratedSourceDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getOutputDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getIconsDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getLibDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getSyntaxDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getEditorDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getJavaDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getJavaTransDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getGeneratedSyntaxDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getTransDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getCacheDirectory() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileObject getMainESVFile() {
        return null;
    }

}
