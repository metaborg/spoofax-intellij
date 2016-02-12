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

package org.metaborg.intellij.jps.serialization;

import org.apache.commons.lang3.builder.*;

import javax.annotation.*;
import java.util.*;

/**
 * Stores project-wide configuration.
 */
public final class SpoofaxProjectState {

    @Nullable
    private String myName;

    /**
     * Initializes a new instance of the {@link SpoofaxProjectState} class.
     */
    public SpoofaxProjectState() {
        // Set defaults here.
    }

    @Nullable
    public final String getMyName() {
        return this.myName;
    }

    public final void setMyName(@Nullable final String value) {
        this.myName = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder(19, 31)
                // Add only final fields here.
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(@Nullable final Object obj) {
        if (!(obj instanceof SpoofaxProjectState))
            return false;
        return equals((SpoofaxProjectState)obj);
    }

    /**
     * Indicates whether the specified object is equal to this one.
     *
     * @param other The other object to test.
     * @return <code>true</code> when the objects are equal;
     * otherwise, <code>false</code>.
     */
    public final boolean equals(@Nullable final SpoofaxProjectState other) {
        if (Objects.equals(this, other))
            return true;
        if (other == null)
            return false;

        return new EqualsBuilder()
                // Add all fields here.
                .append(this.myName, other.myName)
                .isEquals();
    }

}
