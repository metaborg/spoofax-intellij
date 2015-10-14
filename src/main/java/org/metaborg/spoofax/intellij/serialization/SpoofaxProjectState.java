package org.metaborg.spoofax.intellij.serialization;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.annotation.Nullable;

public final class SpoofaxProjectState {

    @Nullable
    private String myName;

    public SpoofaxProjectState() {
        // Defaults
    }

    @Nullable
    public final String getMyName() {
        return this.myName;
    }

    public final void setMyName(@Nullable String value) {
        this.myName = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        return new HashCodeBuilder(19, 31)
                .append(this.myName)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(@Nullable Object obj) {
        if (!(obj instanceof SpoofaxProjectState))
            return false;
        return equals((SpoofaxProjectState) obj);
    }

    public final boolean equals(@Nullable SpoofaxProjectState other) {
        if (other == this)
            return true;
        if (other == null)
            return false;

        return new EqualsBuilder()
                .append(this.myName, other.myName)
                .isEquals();
    }

}
