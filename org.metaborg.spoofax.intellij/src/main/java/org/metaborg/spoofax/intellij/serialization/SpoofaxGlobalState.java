/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.serialization;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nullable;

public final class SpoofaxGlobalState {
    @Nullable
    private String myName;

    public SpoofaxGlobalState() {
        // Defaults
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
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(@Nullable final Object obj) {
        if (!(obj instanceof SpoofaxGlobalState))
            return false;
        return equals((SpoofaxGlobalState)obj);
    }

    public final boolean equals(@Nullable final SpoofaxGlobalState other) {
        if (other == this)
            return true;
        if (other == null)
            return false;

        return new EqualsBuilder()
                .append(this.myName, other.myName)
                .isEquals();
    }

}
