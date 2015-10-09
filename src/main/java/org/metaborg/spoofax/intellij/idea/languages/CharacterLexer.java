package org.metaborg.spoofax.intellij.idea.languages;

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

    @InjectLogger
    private Logger logger;
    @NotNull private final SpoofaxTokenTypeManager tokenTypesManager;

    private CharSequence buffer;
    private int offst;
    private int bufferStart;
    private int bufferEnd;

    //@Inject
    public CharacterLexer(@NotNull final SpoofaxTokenTypeManager tokenTypesManager) {
        this.tokenTypesManager = tokenTypesManager;
    }

    /**
     * Initiates a lexing session.
     * @param buffer The character sequence to lex.
     * @param bufferStart The inclusive start offset.
     * @param bufferEnd The exclusive end offset.
     * @param initialState Not used. Must be zero.
     */
    @Override
    public void start(CharSequence buffer, int bufferStart, int bufferEnd, int initialState) {
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
     * The current token type, or null.
     * @return The current token type, or null when lexing is finished.
     */
    @Nullable
    @Override
    public IElementType getTokenType() {
//        if (!(this.bufferStart <= this.offst && this.offst < this.bufferEnd)) {
//            System.out.println("getTokenType");
//            System.out.println("  Start:  " + bufferStart);
//            System.out.println("  Offset: " + offst);
//            System.out.println("  End:    " + bufferEnd);
//        }
        if (this.bufferStart <= this.offst && this.offst < this.bufferEnd)
            return this.tokenTypesManager.getCharacterTokenType();
        else
            return null;
    }

    @Override
    public int getTokenStart() {
//        if (!(this.bufferStart <= this.offst && this.offst < this.bufferEnd)) {
//            System.out.println("getTokenStart");
//            System.out.println("  Start:  " + bufferStart);
//            System.out.println("  Offset: " + offst);
//            System.out.println("  End:    " + bufferEnd);
//        }
        assert this.bufferStart <= this.offst && this.offst < this.bufferEnd;
        return this.offst;
    }

    @Override
    public int getTokenEnd() {
//        if (!(this.bufferStart <= this.offst && this.offst < this.bufferEnd)) {
//            System.out.println("getTokenEnd");
//            System.out.println("  Start:  " + bufferStart);
//            System.out.println("  Offset: " + offst);
//            System.out.println("  End:    " + bufferEnd);
//        }
        assert this.bufferStart <= this.offst && this.offst < this.bufferEnd;
        return this.offst + 1;
    }

    @Override
    public void advance() {
        this.offst++;
    }

    @Override
    public int getState() {
        // Unused: always zero.
        return 0;
    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return this.buffer;
    }

    @Override
    public int getBufferEnd() {
        return this.bufferEnd;
    }
}