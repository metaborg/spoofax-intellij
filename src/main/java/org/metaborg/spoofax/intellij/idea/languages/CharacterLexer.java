package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

/**
 * Lexer that always lexes a single character.
 */
public final class CharacterLexer extends LexerBase {

    @NotNull
    private final SpoofaxTokenTypeManager tokenTypesManager;
    @InjectLogger
    private Logger logger;
    // The character buffer.
    private CharSequence buffer;
    // Zero-based offset of the start of the relevant character range.
    private int bufferStart;
    // Zero-based offset of the end of the relevant character range.
    private int bufferEnd;
    // The current zero-based offset in the character buffer.
    private int offst;

    @Inject
    private CharacterLexer(@Assisted @NotNull final SpoofaxTokenTypeManager tokenTypesManager) {
        this.tokenTypesManager = tokenTypesManager;
    }

    /**
     * Initiates a lexing session.
     *
     * @param buffer       The character sequence to lex.
     * @param bufferStart  The inclusive start offset.
     * @param bufferEnd    The exclusive end offset.
     * @param initialState Not used. Must be zero.
     */
    @Override
    public void start(@NotNull final CharSequence buffer,
                      final int bufferStart,
                      final int bufferEnd,
                      final int initialState) {
        assert buffer != null;
        assert initialState == 0;
        assert 0 <= bufferStart && bufferStart <= buffer.length();
        assert 0 <= bufferEnd && bufferEnd <= buffer.length();

        this.buffer = buffer;
        this.bufferStart = bufferStart;
        this.bufferEnd = bufferEnd;
        this.offst = bufferStart;
    }

    /**
     * Gets the current state of the lexer.
     *
     * @return An integer that indicates the current state.
     */
    @Override
    public int getState() {
        // Unused: always zero.
        return 0;
    }

    /**
     * The current token type, or <code>null</code>.
     *
     * @return The current token type, or <code>null</code> when lexing is finished.
     */
    @Nullable
    @Override
    public IElementType getTokenType() {
        if (this.bufferStart <= this.offst && this.offst < this.bufferEnd)
            return this.tokenTypesManager.getCharacterTokenType();
        else
            return null;
    }

    /**
     * Gets the start of the current token.
     *
     * @return The zero-based offset of the start of the current token in the character buffer.
     */
    @Override
    public int getTokenStart() {
        assert this.bufferStart <= this.offst && this.offst < this.bufferEnd;
        return this.offst;
    }

    /**
     * Gets the end of the current token.
     *
     * @return The zero-based offset of the end of the current token in the character buffer.
     */
    @Override
    public int getTokenEnd() {
        assert this.bufferStart <= this.offst && this.offst < this.bufferEnd;
        return this.offst + 1;
    }

    /**
     * Advance the lexer to the next token.
     */
    @Override
    public void advance() {
        this.offst++;
    }

    /**
     * Gets the character buffer.
     *
     * @return The character buffer.
     */
    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return this.buffer;
    }

    /**
     * Gets the end of the relevant range of characters.
     *
     * @return The zero-based offset of the end of the relevant range of characters in the character buffer.
     */
    @Override
    public int getBufferEnd() {
        return this.bufferEnd;
    }

}