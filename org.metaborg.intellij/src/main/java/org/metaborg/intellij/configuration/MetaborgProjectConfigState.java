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

import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.jps.configuration.*;

import javax.annotation.*;
import java.util.*;

/**
 * State of the project-level configurations.
 *
 * Don't use this class directly. Instead use the {@link IdeaMetaborgProjectConfig} class (in IntelliJ IDEA)
 * or the {@link JpsMetaborgProjectConfig} class (in JPS).
 */
@SuppressWarnings("PublicField")
public final class MetaborgProjectConfigState {

    // The fields must be public non-final, and use only simple types:
    //   numbers, booleans, strings, collections, maps, enums
    // Add all fields to the comparison in equals().

    public String myName;

    /**
     * Initializes a new instance of the {@link MetaborgProjectConfigState} class.
     */
    public MetaborgProjectConfigState() {
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
        } if (obj instanceof MetaborgProjectConfigState) {
            // Same type.
            return equals((MetaborgProjectConfigState)obj);
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
    public boolean equals(@Nullable final MetaborgProjectConfigState other) {
        if (other == this) return true;
        if (other == null) return false;

        // Compare the fields here.
        return Objects.equals(this.myName, other.myName);
    }

}
