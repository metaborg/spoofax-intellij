package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a Spoofax token. This class contains enough information
 * to reconstruct the Spoofax AST.
 */
public final class SpoofaxToken {

    @NotNull
    public final SpoofaxTokenType type;
    public final int start;
    public final int end;

    /**
     * Initializes a new instance of the {@link SpoofaxToken} class.
     *
     * @param type  The token type.
     * @param start The inclusive start offset of the token.
     * @param end   The exclusive end offset of the token.
     */
    public SpoofaxToken(@NotNull final SpoofaxTokenType type, final int start, final int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return type.toString() + " [" + start + ", " + end + ")";
    }

}