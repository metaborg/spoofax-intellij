package org.metaborg.spoofax.intellij.languages.syntax;

import com.intellij.psi.tree.IElementType;
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

    public SpoofaxToken(@NotNull SpoofaxTokenType type, int start, int end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return type.toString() + " [" + start + ", " + end + ")";
    }

}