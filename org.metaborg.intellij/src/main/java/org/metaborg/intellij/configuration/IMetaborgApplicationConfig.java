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

package org.metaborg.intellij.configuration;

import com.google.common.collect.*;
import com.google.inject.*;
import com.intellij.openapi.components.*;
import org.apache.commons.vfs2.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.discovery.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import java.util.*;

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
