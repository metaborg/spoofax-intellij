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

/**
 * Language management functionality.
 *
 * Metaborg Core does most of the language management. However, since we need to execute
 * additional steps before a language can be used in IntelliJ IDEA, we need to have control
 * over language loading, unloading, activation and deactivation. That's done in implementations
 * of the {@link org.metaborg.intellij.idea.languages.ILanguageManager} interface.
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