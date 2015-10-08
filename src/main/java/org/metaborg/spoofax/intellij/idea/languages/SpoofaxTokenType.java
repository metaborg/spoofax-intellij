package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.style.IStyle;

/**
 * The type of a Spoofax token.
 */
public final class SpoofaxTokenType extends IElementType {

    @NotNull private final IStyle style;

    /**
     * Gets the style associated with this token type.
     *
     * @return The token style.
     */
    @NotNull public final IStyle getStyle() { return this.style; }

    /**
     * Initializes a new instance of the {@link SpoofaxTokenType} class.
     *
     * @param language The associated IDEA language.
     * @param style The token style.
     */
    public SpoofaxTokenType(@NotNull SpoofaxIdeaLanguage language, @NotNull IStyle style) {
        super(style.toString(), language);
        this.style = style;
    }

    @Override
    public String toString() {
        return SpoofaxTokenType.class.getName() + "." + super.toString() + "." + this.style.toString();
    }

}
