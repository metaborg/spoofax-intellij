package org.metaborg.settings;

public final class ComplexObject {
    public final String name;
    public final int value;

    public ComplexObject(final String name, final int value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ComplexObject)
            return equals((ComplexObject)obj);
        return false;
    }

    public boolean equals(final ComplexObject other) {
        if (other == null)
            return false;
        return this.name.equals(other.name)
                && this.value == other.value;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
