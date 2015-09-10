package org.metaborg.spoofax.intellij.serialization;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public final class SpoofaxGlobalState {
    private String myName;
    public String getMyName() { return this.myName; }
    public void setMyName(String value) { this.myName = value; }

    public SpoofaxGlobalState() {
        // Defaults
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpoofaxGlobalState))
            return false;
        return equals((SpoofaxGlobalState)obj);
    }

    public boolean equals(SpoofaxGlobalState other) {
        if (other == this)
            return true;
        if (other == null)
            return false;

        return new EqualsBuilder()
                .append(this.myName, other.myName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 31)
                .append(this.myName)
                .toHashCode();
    }

}
