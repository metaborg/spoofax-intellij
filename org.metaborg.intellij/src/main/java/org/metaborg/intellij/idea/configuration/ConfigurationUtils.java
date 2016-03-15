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

package org.metaborg.intellij.idea.configuration;

import com.google.inject.*;
import com.intellij.openapi.command.*;
import com.intellij.openapi.project.Project;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.logging.*;
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
        this.logger.debug("Loading languages: {}", ids);
        final Collection<ILanguageComponent> components;
        try {
            components = this.languageManager.discoverRange(ids);
        } catch (final LanguageLoadingFailedException e) {
            this.logger.error("Could not discover languages: {}", e, ids);
            return;
        }
        this.logger.info("Loaded languages: {}", components);
        final Set<ILanguage> languages = LanguageUtils2.getLanguagesOfComponents(components);
        this.logger.debug("Activating languages: {}", languages);
        this.languageManager.activateRange(languages);
        this.logger.info("Activated languages: {}", languages);
    }
}
