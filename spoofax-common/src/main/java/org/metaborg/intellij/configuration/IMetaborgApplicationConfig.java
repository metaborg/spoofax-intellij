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

package org.metaborg.intellij.configuration;

import org.metaborg.core.language.*;

import java.util.Set;

/**
 * Application-level configuration of the plugin.
 */
public interface IMetaborgApplicationConfig {

    String CONFIG_NAME = "MetaborgApplicationConfig";
    String CONFIG_FILE = "metaborg.xml";

    /**
     * Gets the mutable set of identifiers of languages that should be loaded and activated.
     *
     * @return A mutable set of language identifiers.
     */
    Set<LanguageIdentifier> getLoadedLanguages();

}
