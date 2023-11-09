/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.parsing;


import com.google.inject.assistedinject.Assisted;
import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;
import org.metaborg.intellij.idea.parsing.elements.SpoofaxTokenTypeManager;
import org.metaborg.intellij.logging.InjectLogger;
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

    @jakarta.inject.Inject @javax.inject.Inject
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