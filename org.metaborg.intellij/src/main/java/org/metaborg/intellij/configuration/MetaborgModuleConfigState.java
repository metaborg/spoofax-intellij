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

import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.jps.configuration.*;

import javax.annotation.*;
import java.util.*;

/**
 * State of the module-level configurations.
 *
 * Don't use this class directly. Instead use the {@link IdeaMetaborgModuleConfig} class (in IntelliJ IDEA)
 * or the {@link JpsMetaborgModuleConfig} class (in JPS).
 */
@SuppressWarnings("PublicField")
public final class MetaborgModuleConfigState {

    // The fields must be public non-final, and use only simple types:
    //   numbers, booleans, strings, collections, maps, enums
    // Add all fields to the comparison in equals().

    public String myName;

    /**
     * Initializes a new instance of the {@link MetaborgModuleConfigState} class.
     */
    public MetaborgModuleConfigState() {
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
        } if (obj instanceof MetaborgModuleConfigState) {
            // Same type.
            return equals((MetaborgModuleConfigState)obj);
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
    public boolean equals(@Nullable final MetaborgModuleConfigState other) {
        if (other == this) return true;
        if (other == null) return false;

        // Compare the fields here.
        return Objects.equals(this.myName, other.myName);
    }

}
