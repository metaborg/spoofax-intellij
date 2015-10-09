package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.util.proxy.ProxyFactory;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Creates and caches {@link IdeaLanguageAttachment} objects.
 */
@Singleton
public final class IdeaAttachmentManager implements IIdeaAttachmentManager {

    @InjectLogger
    private Logger logger;
    @NotNull
    private final ProxyFactory proxyFactory;
    @NotNull
    private final ISpoofaxLexerAdapterFactory lexerFactory;
    @NotNull
    private final ISpoofaxParserDefinitionFactory parserDefinitionFactory;
    @NotNull
    private final HashMap<ILanguage, IdeaLanguageAttachment> languages = new HashMap<>();
    @NotNull
    private final HashMap<ILanguageImpl, IdeaLanguageImplAttachment> implementations = new HashMap<>();

    @Inject
    private IdeaAttachmentManager(@NotNull final ISpoofaxLexerAdapterFactory lexerFactory,
                                  @NotNull final ISpoofaxParserDefinitionFactory parserDefinitionFactory) {
        this.lexerFactory = lexerFactory;
        this.parserDefinitionFactory = parserDefinitionFactory;

        this.proxyFactory = new ProxyFactory();
        this.proxyFactory.setUseCache(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdeaLanguageAttachment get(ILanguage language) {
        IdeaLanguageAttachment obj = this.languages.get(language);
        if (obj == null) {
            obj = createLanguageAttachment(language);
            this.languages.put(language, obj);
            logger.info("Created a new IdeaLanguageAttachment for language {}.", language);
        }
        else {
            logger.info("Used cached IdeaLanguageAttachment for language {}.", language);
        }
        return obj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdeaLanguageImplAttachment get(ILanguageImpl implementation) {
        IdeaLanguageImplAttachment obj = this.implementations.get(implementation);
        if (obj == null) {
            obj = createLanguageImplAttachment(implementation);
            this.implementations.put(implementation, obj);
            logger.info("Created a new IdeaLanguageImplAttachment for language implementation {}.", implementation);
        }
        else {
            logger.info("Used cached IdeaLanguageImplAttachment for language implementation {}.", implementation);
        }
        return obj;
    }

    /**
     * Creates a new {@link IdeaLanguageAttachment}.
     *
     * @param language The language.
     * @return The created {@link IdeaLanguageAttachment}.
     */
    @NotNull
    private IdeaLanguageAttachment createLanguageAttachment(@NotNull final ILanguage language) {
        final SpoofaxIdeaLanguage ideaLanguage = createIdeaLanguage(language);
        final SpoofaxFileType fileType = createFileType(ideaLanguage);
        final SpoofaxTokenTypeManager tokenTypeManager = createTokenTypeManager(ideaLanguage);
        final OldSpoofaxTokenType dummyAstTokenType = createDummyAstTokenType(ideaLanguage);
        final SpoofaxParserDefinition parserDefinition = createParserDefinition(fileType);
        final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory = createSyntaxHighlighterFactory(parserDefinition);

        return new IdeaLanguageAttachment(ideaLanguage, fileType, tokenTypeManager, dummyAstTokenType, parserDefinition, syntaxHighlighterFactory);
    }

    /**
     * Creates a new {@link IdeaLanguageImplAttachment}.
     *
     * @param implementation The language implementation.
     * @return The created {@link IdeaLanguageImplAttachment}.
     */
    @NotNull
    private IdeaLanguageImplAttachment createLanguageImplAttachment(@NotNull final ILanguageImpl implementation) {
        final IdeaLanguageAttachment langAtt = get(implementation.belongsTo());

        final SpoofaxLexer lexer = createLexer(implementation, langAtt.tokenTypeManager);
        final OldSpoofaxParser parser = createParser(langAtt.dummyAstTokenType);

        return new IdeaLanguageImplAttachment(lexer, parser);
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
     *
     * @param fileType The language file type.
     * @return The created parser definition.
     */
    @NotNull
    private final SpoofaxParserDefinition createParserDefinition(@NotNull final SpoofaxFileType fileType) {
        return this.parserDefinitionFactory.create(fileType);
    }

    /**
     * Creates a new lexer.
     *
     * @param language The language.
     * @return The lexer.
     */
    @NotNull
    private final SpoofaxLexer createLexer(@NotNull final ILanguageImpl language, @NotNull final SpoofaxTokenTypeManager tokenTypeManager) {
        return this.lexerFactory.create(language, tokenTypeManager);
    }

    /**
     * Creates a new parser.
     *
     * @param dummyAstTokenType The dummy AST token type.
     * @return The parser.
     */
    @NotNull
    private final OldSpoofaxParser createParser(@NotNull final OldSpoofaxTokenType dummyAstTokenType) {
        return new OldSpoofaxParser(dummyAstTokenType);
    }

    /**
     * Creates a new syntax highlighter factory.
     *
     * @param parserDefinition The parser definition.
     * @return The syntax highlighter factory.
     */
    @NotNull
    private final SpoofaxSyntaxHighlighterFactory createSyntaxHighlighterFactory(@NotNull final SpoofaxParserDefinition parserDefinition) {
        return new SpoofaxSyntaxHighlighterFactory(parserDefinition);
    }
}
