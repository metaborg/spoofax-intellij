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

package org.metaborg.intellij.idea.configuration;

import com.google.inject.*;
import com.intellij.openapi.command.*;
import com.intellij.openapi.project.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.idea.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.resources.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

/**
 * Configuration utility functions.
 */
public final class ConfigurationUtils {

    private final IIdeaLanguageManager languageManager;
    @InjectLogger
    private ILogger logger;

    @Inject
    public ConfigurationUtils(final IIdeaLanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    /**
     * Loads and activates the specified languages.
     *
     * @param project The project.
     * @param ids The language IDs.
     */
    public void loadAndActivateLanguages(@Nullable final Project project, final Collection<LanguageIdentifier> ids) {
        if (project != null) {
            WriteCommandAction.runWriteCommandAction(project, () -> {
                loadAndActivateLanguagesInternal(ids);
            });
        } else {
            loadAndActivateLanguagesInternal(ids);
        }
    }

    /**
     * Loads and activates the specified languages.
     *
     * This method must be executed in a write action.
     *
     * @param ids The language IDs.
     */
    private void loadAndActivateLanguagesInternal(final Collection<LanguageIdentifier> ids) {
        this.logger.debug("Loading module languages: {}", ids);
        final Collection<ILanguageComponent> components;
        try {
            components = this.languageManager.discoverRange(ids);
        } catch (final LanguageLoadingFailedException e) {
            this.logger.error("Could not discover languages: {}", e, ids);
            return;
        }
        this.logger.info("Loaded module languages: {}", components);
        final Set<ILanguage> languages = LanguageUtils2.getLanguagesOfComponents(components);
        this.logger.debug("Activating module languages: {}", languages);
        this.languageManager.activateRange(languages);
        this.logger.info("Activated module languages: {}", languages);
    }

}
