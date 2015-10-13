package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.style.IStyle;
import org.metaborg.core.style.Style;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Tracks token types.
 * <p>
 * IntelliJ can't handle too many token types. However, we'll still need a different token type for each different
 * style. This class returns a token type that matches the style, if found; otherwise, creates a new token type and
 * stores it for re-use.
 */
public final class SpoofaxTokenTypeManager {

    @NotNull
    private final Map<IStyle, SpoofaxTokenType> map = new HashMap<>();
    @NotNull
    private final SpoofaxIdeaLanguage language;
    @NotNull
    private final CharacterTokenType characterTokenType;

    /**
     * Initializes a new instance of the {@link SpoofaxTokenTypeManager} class.
     *
     * @param language The IDEA language.
     */
    public SpoofaxTokenTypeManager(@NotNull SpoofaxIdeaLanguage language) {
        this.language = language;
        this.characterTokenType = new CharacterTokenType(language);
    }

    /**
     * Returns a special token type used to represent single character tokens.
     *
     * @return The token type.
     */
    @NotNull
    public CharacterTokenType getCharacterTokenType() {
        return this.characterTokenType;
    }

    /**
     * Returns the token type corresponding to the specified style.
     *
     * @param style The style of the token type. The returned token type will have this style. The style may be null,
     *              in which case the default token style is assumed.
     * @return The token type.
     */
    @NotNull
    public SpoofaxTokenType getTokenType(@Nullable IStyle style) {
        style = style != null ? style : getDefaultStyle();

        SpoofaxTokenType token = this.map.getOrDefault(style, null);
        if (token == null) {
            token = new SpoofaxTokenType(this.language, style);
            this.map.put(style, token);
        }
        assert token != null;
        assert token.getStyle().equals(style);

        return token;
    }

    /**
     * Gets the default style when no style is specified.
     *
     * @return The default style.
     */
    @NotNull
    public IStyle getDefaultStyle() {
        // TODO: Get this from the IStylerService?
        return new Style(Color.black, Color.white, false, false, false);
    }
}