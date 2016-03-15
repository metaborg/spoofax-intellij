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

/**
 * Language management functionality.
 *
 * Metaborg Core does most of the language management. However, since we need to execute
 * additional steps before a language can be used in IntelliJ IDEA, we need to have control
 * over language loading, unloading, activation and deactivation. That's done in implementations
 * of the {@link org.metaborg.intellij.languages.ILanguageManager} interface.
 *
 * Loading and unloading a language ultimately calls upon Metaborg Core. Activating a language
 * will install the language in IDEA. This involves, among other things, registering the file
 * type, lexer, parser, highlighter and annotator for the language. Deactivating a language
 * unregisters those same things.
 *
 * Many objects in IntelliJ IDEA are identified through an identifier, and there is a limit
 * on how many identifiers are supported (e.g. 65536 different identifiers). Therefore we can't
 * just create and register objects whenever we feel like it. Instead, we have to be very
 * careful about how many object we create and register, and reuse them whenever possible.
 */
@NonNullByDefault
package org.metaborg.intellij.idea.languages;

import org.metaborg.intellij.*;