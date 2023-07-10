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

import org.metaborg.intellij.UnhandledException;
import org.metaborg.intellij.projects.MetaborgModuleConstants;
import org.metaborg.util.resource.ResourceUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * State of the application-level configuration of the plugin.
 *
 * Don't use this class directly. Instead use the IdeaMetaborgApplicationConfig class (in IntelliJ IDEA)
 * or the JpsMetaborgApplicationConfig class (in JPS).
 */
@SuppressWarnings("PublicField")
public final class MetaborgApplicationConfigState {

    // The fields must be public non-final, and use only simple types:
    //   numbers, booleans, strings, collections, maps, enums
    // Add all fields to the comparison in equals().

    /**
     * The IDs of the loaded languages.
     */
    public Set<String> loadedLanguages = new HashSet<>();

    /**
     * Initializes a new instance of the {@link MetaborgApplicationConfigState} class.
     */
    public MetaborgApplicationConfigState() {
        // Default configuration:
        this.loadedLanguages = getDefaultLoadedLanguages();
    }

    /**
     * Reads the list of language IDs from the default_languages.txt resource file.
     *
     * @return A set of language IDs.
     */
    private static Set<String> getDefaultLoadedLanguages() {
        // Specify the default loaded languages in this file as a list of language IDs, one on each line.
        final String text;
        final URL url = Objects.requireNonNull(MetaborgModuleConstants.class.getResource("/default_languages.txt"));
        try {
            text = ResourceUtils.readInputStream(url.openStream(), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new UnhandledException("Cannot get resource content of resource: " + url, e);
        }

        final String[] ids = text.split("\\r?\\n");

        return new HashSet<>(Arrays.asList(ids));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            // Same instance.
            return true;
        } if (obj instanceof MetaborgApplicationConfigState) {
            // Same type.
            return equals((MetaborgApplicationConfigState)obj);
        } else {
            // Different.
            return false;
        }
    }

    /**
     * Determines whether this instance and the specified instance are equal.
     *
     * @param other The other instance.
     * @return <code>true</code> when the instances are equal;
     * otherwise, <code>false</code>.
     */
    public boolean equals(@Nullable final MetaborgApplicationConfigState other) {
        if (other == this) return true;
        if (other == null) return false;

        // Compare the fields here.
        if (this.loadedLanguages.size() != other.loadedLanguages.size()) {
          return false;
        }
        Iterator<?> iterator1 = this.loadedLanguages.iterator();
        Iterator<?> iterator2 = other.loadedLanguages.iterator();
        while (iterator1.hasNext()) {
          if (!iterator2.hasNext()) {
            return false;
          }
          Object o1 = iterator1.next();
          Object o2 = iterator2.next();
          if (!Objects.equals(o1, o2)) {
            return false;
          }
        }
        return !iterator2.hasNext();
    }
}
