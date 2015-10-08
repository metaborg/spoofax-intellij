package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.util.proxy.ProxyFactory;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.languages.SpoofaxFileType;
import org.metaborg.spoofax.intellij.languages.SpoofaxIdeaLanguage;
import org.metaborg.spoofax.intellij.languages.syntax.*;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Creates and caches {@link IdeaLanguageObject} objects.
 */
@Singleton
public final class IdeaLanguageObjectManager {

    @InjectLogger
    private Logger logger;
    @NotNull
    private final ProxyFactory proxyFactory;
    @NotNull
    private final ISpoofaxLexerAdapterFactory lexerFactory;
    @NotNull
    private final HashMap<ILanguage, IdeaLanguageObject> objects = new HashMap<>();

    @Inject
    private IdeaLanguageObjectManager(@NotNull final ISpoofaxLexerAdapterFactory lexerFactory) {
        this.lexerFactory = lexerFactory;

        this.proxyFactory = new ProxyFactory();
        this.proxyFactory.setUseCache(false);
    }

    /**
     * Gets the {@link IdeaLanguageObject} for a particular language.
     *
     * If no {@link IdeaLanguageObject} exists yet for the language,
     * a new  {@link IdeaLanguageObject} is created and cached.
     *
     * @param language The language.
     * @return The corresponding {@link IdeaLanguageObject}.
     */
    public IdeaLanguageObject get(ILanguage language) {
        IdeaLanguageObject obj = this.objects.get(language);
        if (obj == null) {
            obj = create(language);
            this.objects.put(language, obj);
            logger.info("Created a new IdeaLanguageObject for language.", language);
        }
        else {
            logger.info("Used cached IdeaLanguageObject for language.", language);
        }
        return obj;
    }

    /**
     * Creates a new {@link IdeaLanguageObject}.
     * @param language The language.
     * @return The created {@link IdeaLanguageObject}.
     */
    @NotNull
    private IdeaLanguageObject create(@NotNull final ILanguage language) {
        final SpoofaxIdeaLanguage ideaLanguage = createIdeaLanguage(language);
        final SpoofaxFileType fileType = createFileType(ideaLanguage);
        final SpoofaxTokenTypeManager tokenTypeManager = createTokenTypeManager(ideaLanguage);
        final OldSpoofaxTokenType dummyAstTokenType = createDummyAstTokenType(ideaLanguage);
        final SpoofaxLexer lexer = createLexer(language.activeImpl(), tokenTypeManager);
        final OldSpoofaxParser parser = createParser(dummyAstTokenType);
        final SpoofaxParserDefinition parserDefinition = createParserDefinition(fileType, lexer, parser);
        final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory = createSyntaxHighlighterFactory(parserDefinition);

        return new IdeaLanguageObject(ideaLanguage, fileType, tokenTypeManager, dummyAstTokenType, parserDefinition, lexer, parser, syntaxHighlighterFactory);
    }

    /**
     * Creates a new IDEA language for a Spoofax language.
     *
     * @param language The language.
     * @return The created IDEA language.
     */
    @NotNull
    private final SpoofaxIdeaLanguage createIdeaLanguage(@NotNull final ILanguage language) {
        try {
            this.proxyFactory.setSuperclass(SpoofaxIdeaLanguage.class);
            return (SpoofaxIdeaLanguage)this.proxyFactory.create(new Class<?>[]{ ILanguage.class }, new Object[]{ language });
        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            this.logger.error("Unexpected unhandled exception.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new file type for an IDEA language.
     *
     * @param language The IDEA language.
     * @return The created file type.
     */
    @NotNull
    private final SpoofaxFileType createFileType(@NotNull final SpoofaxIdeaLanguage language) {
        try {
            this.proxyFactory.setSuperclass(SpoofaxFileType.class);
            return (SpoofaxFileType)this.proxyFactory.create(new Class<?>[]{ SpoofaxIdeaLanguage.class }, new Object[]{ language });
        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            this.logger.error("Unexpected unhandled exception.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new spoofax token type manager for an IDEA language.
     *
     * @param language The IDEA language.
     * @return The created token type manager.
     */
    @NotNull
    private final SpoofaxTokenTypeManager createTokenTypeManager(@NotNull final SpoofaxIdeaLanguage language) {
        return new SpoofaxTokenTypeManager(language);
    }

    /**
     * Creates a new dummy AST token type.
     *
     * @param language The IDEA language.
     * @return The created dummy token type.
     */
    @NotNull
    private final OldSpoofaxTokenType createDummyAstTokenType(@NotNull final SpoofaxIdeaLanguage language) {
        return new OldSpoofaxTokenType("DUMMY", language);
    }

    /**
     * Creates a new parser definition.
     * @param fileType The language file type.
     * @return The created parser definition.
     */
    @NotNull
    private final SpoofaxParserDefinition createParserDefinition(@NotNull final SpoofaxFileType fileType, @NotNull final SpoofaxLexer lexer, @NotNull final OldSpoofaxParser parser) {
        return new SpoofaxParserDefinition(fileType, lexer, parser);
    }

    /**
     * Creates a new lexer.
     * @param language The language.
     * @return The lexer.
     */
    @NotNull
    private final SpoofaxLexer createLexer(@NotNull final ILanguageImpl language, @NotNull final SpoofaxTokenTypeManager tokenTypeManager) {
        return this.lexerFactory.create(language, tokenTypeManager);
    }

    /**
     * Creates a new parser.
     * @param dummyAstTokenType The dummy AST token type.
     * @return The parser.
     */
    @NotNull
    private final OldSpoofaxParser createParser(@NotNull final OldSpoofaxTokenType dummyAstTokenType) {
        return new OldSpoofaxParser(dummyAstTokenType);
    }

    /**
     * Creates a new syntax highlighter factory.
     * @param parserDefinition The parser definition.
     * @return The syntax highlighter factory.
     */
    @NotNull
    private final SpoofaxSyntaxHighlighterFactory createSyntaxHighlighterFactory(@NotNull final SpoofaxParserDefinition parserDefinition) {
        return new SpoofaxSyntaxHighlighterFactory(parserDefinition);
    }
}
