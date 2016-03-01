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
import org.apache.commons.collections.*;
import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.jps.configuration.*;

import javax.annotation.*;
import java.util.*;

/**
 * State of the application-level configuration of the plugin.
 *
 * Don't use this class directly. Instead use the {@link IdeaMetaborgApplicationConfig} class (in IntelliJ IDEA)
 * or the {@link JpsMetaborgApplicationConfig} class (in JPS).
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
        this.loadedLanguages = Sets.newHashSet(
//                "org.metaborg:org.metaborg.meta.lang.sdf:2.0.0-SNAPSHOT",
//                "org.metaborg:org.metaborg.meta.lang.esv:1.5.0-baseline-20150917-172646",
//                "org.metaborg:org.metaborg.meta.lang.esv:2.0.0-SNAPSHOT",
//                "org.metaborg:org.metaborg.meta.lang.nabl:2.0.0-SNAPSHOT",
//                "org.metaborg:org.metaborg.meta.lang.sdf:1.5.0-baseline-20150917-172646",
//                "org.metaborg:org.metaborg.meta.lang.sdf:2.0.0-SNAPSHOT",
//                "org.metaborg:org.metaborg.meta.lang.stratego:1.5.0-baseline-20150917-172646",
//                "org.metaborg:org.metaborg.meta.lang.template:2.0.0-SNAPSHOT",
//                "org.metaborg:org.metaborg.meta.lang.ts:2.0.0-SNAPSHOT",
//                "org.metaborg:org.metaborg.meta.lib.analysis:2.0.0-SNAPSHOT"
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
        return CollectionUtils.isEqualCollection(this.loadedLanguages, other.loadedLanguages);
    }
}
