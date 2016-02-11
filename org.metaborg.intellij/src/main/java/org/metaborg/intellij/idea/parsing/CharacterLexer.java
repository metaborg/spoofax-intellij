/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.intellij.idea.parsing;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.lexer.*;
import com.intellij.psi.tree.*;
import org.jetbrains.annotations.*;
import org.metaborg.intellij.idea.parsing.elements.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

/**
 * Lexer that always lexes a single character.
 */
public final class CharacterLexer extends LexerBase {

    private final SpoofaxTokenTypeManager tokenTypesManager;
    @InjectLogger
    private ILogger logger;
    // The character buffer.
    private CharSequence buffer;
    // Zero-based offset of the start of the relevant character range.
    private int bufferStart;
    // Zero-based offset of the end of the relevant character range.
    private int bufferEnd;
    // The current zero-based offset in the character buffer.
    private int offst;

    @Inject
    private CharacterLexer(@Assisted final SpoofaxTokenTypeManager tokenTypesManager) {
        super();
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
    public void start(
            final CharSequence buffer,
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
        assert this.bufferStart <= this.offst && this.offst < this.bufferEnd : this.logger.format(
                "Offset {} must be between [{}, {}).",
                this.offst,
                this.bufferStart,
                this.bufferEnd
        );
        return this.offst;
    }

    /**
     * Gets the end of the current token.
     *
     * @return The zero-based offset of the end of the current token in the character buffer.
     */
    @Override
    public int getTokenEnd() {
        assert this.bufferStart <= this.offst && this.offst < this.bufferEnd : this.logger.format(
                "Offset {} must be between [{}, {}).",
                this.offst,
                this.bufferStart,
                this.bufferEnd
        );
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