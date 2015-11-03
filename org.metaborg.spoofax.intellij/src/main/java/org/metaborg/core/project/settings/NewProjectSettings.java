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

import org.jetbrains.annotations.NotNull;
import org.metaborg.settings.*;
import org.metaborg.core.language.LanguageContributionIdentifier;
import org.metaborg.core.language.LanguageIdentifier;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Settings for a project.
 */
public class NewProjectSettings extends Settings implements IProjectSettings {

    /* package private */ static final SettingKey<LanguageIdentifier> IDENTIFIER_KEY
            = new SettingKey<>("id", LanguageIdentifier.class);
    /* package private */ static final SettingKey<String> NAME_KEY
            = new SettingKey<>("name", String.class);
    /* package private */ static final SettingKey<List<LanguageIdentifier>> COMPILE_DEPENDENCIES_KEY
            = new SettingKey<>("compileDependencies", new TypeReference<List<LanguageIdentifier>>() {},
                               SettingStrategies.appendList());
    /* package private */ static final SettingKey<List<LanguageIdentifier>> RUNTIME_DEPENDENCIES_KEY
            = new SettingKey<>("runtimeDependencies", new TypeReference<List<LanguageIdentifier>>() {}, SettingStrategies.appendList());
    /* package private */ static final SettingKey<List<LanguageContributionIdentifier>> LANGUAGE_CONTRIBUTIONS_KEY
            = new SettingKey<>("languageContributions", new TypeReference<List<LanguageContributionIdentifier>>() {}, SettingStrategies.appendList());

    /**
     * Initializes a new instance of the {@link NewProjectSettings} class.
     *
     * @param settings The map of settings to use.
     * @param parent The parent settings; or <code>null</code>.
     */
    /* package private */ NewProjectSettings(@NotNull final Map<ISettingKey<?>, Object> settings, @Nullable final Settings parent) {
        super(settings, parent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LanguageIdentifier identifier() {
        return getSetting(IDENTIFIER_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name() {
        return getSetting(NAME_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<LanguageIdentifier> compileDependencies() {
        return getSetting(COMPILE_DEPENDENCIES_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<LanguageIdentifier> runtimeDependencies() {
        return getSetting(RUNTIME_DEPENDENCIES_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<LanguageContributionIdentifier> languageContributions() {
        return getSetting(LANGUAGE_CONTRIBUTIONS_KEY);
    }
}
