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

import com.google.common.collect.*;
import org.apache.commons.collections.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

/**
 * Application-level configuration of the plugin.
 */
public final class MetaborgApplicationConfig implements Serializable {

    // TODO: Make this field private. (Needs annotation?)
    // TODO: Store LanguageIdentifier objects instead.
    /**
     * The IDs of the loaded languages.
     */
    public Collection<String> loadedLanguages;

    /**
     * Initializes a new instance of the {@link MetaborgApplicationConfig} class.
     */
    public MetaborgApplicationConfig() {
        // Default configuration:
        this.loadedLanguages = Lists.newArrayList(
                "org.metaborg:org.metaborg.meta.lang.sdf:1.5.0-SNAPSHOT"
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            // Same instance.
            return true;
        } if (obj instanceof MetaborgApplicationConfig) {
            // Same type.
            return equals((MetaborgApplicationConfig)obj);
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
    public boolean equals(@Nullable final MetaborgApplicationConfig other) {
        if (other == this) return true;
        if (other == null) return false;

        return CollectionUtils.isEqualCollection(this.loadedLanguages, other.loadedLanguages);
    }
}
