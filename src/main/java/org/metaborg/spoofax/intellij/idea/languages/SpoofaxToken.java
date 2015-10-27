package org.metaborg.spoofax.intellij.idea.languages;

import org.apache.commons.lang3.Range;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Spoofax token. This class contains enough information
 * to reconstruct the Spoofax AST.
 */
public final class SpoofaxToken {

    @NotNull
    public final SpoofaxTokenType type;
//    public final int start;
//    public final int end;
    @NotNull
    public final Range<Integer> range;

    /**
     * Initializes a new instance of the {@link SpoofaxToken} class.
     *
     * @param type  The token type.
     * @param range The range of the token.
     */
    public SpoofaxToken(@NotNull final SpoofaxTokenType type, @NotNull final Range<Integer> range) {//final int start, final int end) {
        this.type = type;
        this.range = range;
//        this.start = start;
//        this.end = end;
    }

    @Override
    public String toString() {
        return type.toString() + " [" + range.getMinimum() + ", " + (range.getMaximum() + 1) + ")";
    }

}