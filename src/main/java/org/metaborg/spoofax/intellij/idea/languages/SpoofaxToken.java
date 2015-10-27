package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.IntRange;

/**
 * Represents a Spoofax token. This class contains enough information
 * to reconstruct the Spoofax AST.
 */
public final class SpoofaxToken {

    @NotNull
    public final SpoofaxTokenType type;
    @NotNull
    public final IntRange range;

    /**
     * Initializes a new instance of the {@link SpoofaxToken} class.
     *
     * @param type  The token type.
     * @param range The range of the token.
     */
    public SpoofaxToken(@NotNull final SpoofaxTokenType type, @NotNull final IntRange range) {
        this.type = type;
        this.range = range;
    }

    @Override
    public String toString() {
        return type.toString() + " [" + range.start + ", " + range.end + ")";
    }

}