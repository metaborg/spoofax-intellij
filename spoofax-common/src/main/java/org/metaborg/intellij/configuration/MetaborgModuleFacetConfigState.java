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

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * State of the module-level facet configurations.
 *
 * Don't use this class directly. Instead use the IdeaMetaborgModuleFacetConfig class (in IntelliJ IDEA)
 * or the JpsMetaborgModuleFacetConfig class (in JPS).
 */
@SuppressWarnings("PublicField")
public final class MetaborgModuleFacetConfigState {

    // The fields must be public non-final, and use only simple types:
    //   numbers, booleans, strings, collections, maps, enums
    // Add all fields to the comparison in equals().

    public String myName;

    /**
     * Initializes a new instance of the {@link MetaborgModuleFacetConfigState} class.
     */
    public MetaborgModuleFacetConfigState() {
        // Default configuration:
        this.myName = "default";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            // Same instance.
            return true;
        } if (obj instanceof MetaborgModuleFacetConfigState) {
            // Same type.
            return equals((MetaborgModuleFacetConfigState)obj);
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
    public boolean equals(@Nullable final MetaborgModuleFacetConfigState other) {
        if (other == this) return true;
        if (other == null) return false;

        // Compare the fields here.
        return Objects.equals(this.myName, other.myName);
    }

}
