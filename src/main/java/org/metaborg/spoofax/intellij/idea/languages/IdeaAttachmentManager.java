/*
 * Copyright © 2015-2015
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

package org.metaborg.spoofax.intellij.idea.languages;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.intellij.lang.ParserDefinition;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import javassist.util.proxy.ProxyFactory;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.factories.ICharacterLexerFactory;
import org.metaborg.spoofax.intellij.factories.IHighlightingLexerFactory;
import org.metaborg.spoofax.intellij.factories.IParserDefinitionFactory;
import org.metaborg.spoofax.intellij.idea.vfs.SpoofaxFileType;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.spoofax.intellij.menu.BuilderMenuBuilder;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Creates and caches {@link IdeaLanguageAttachment} objects.
 */
@Singleton
public final class IdeaAttachmentManager implements IIdeaAttachmentManager {

    @NotNull
    private final ProxyFactory proxyFactory;
    @NotNull
    private final IHighlightingLexerFactory lexerFactory;
    @NotNull
    private final IParserDefinitionFactory parserDefinitionFactory;
    @NotNull
    private final ICharacterLexerFactory characterLexerFactory;
    @NotNull
    private final BuilderMenuBuilder builderMenuBuilder;
    @NotNull
    private final Provider<SpoofaxSyntaxHighlighterFactory> syntaxHighlighterFactoryProvider;
    @NotNull
    private final HashMap<ILanguage, IdeaLanguageAttachment> languages = new HashMap<>();
    @NotNull
    private final HashMap<ILanguageImpl, IdeaLanguageImplAttachment> implementations = new HashMap<>();
    @InjectLogger
    private Logger logger;

    @Inject
    private IdeaAttachmentManager(@NotNull final IHighlightingLexerFactory lexerFactory,
                                  @NotNull final IParserDefinitionFactory parserDefinitionFactory,
                                  @NotNull final ICharacterLexerFactory characterLexerFactory,
                                  @NotNull final BuilderMenuBuilder builderMenuBuilder,
                                  @NotNull final Provider<SpoofaxSyntaxHighlighterFactory> syntaxHighlighterFactoryProvider) {
        this.lexerFactory = lexerFactory;
        this.parserDefinitionFactory = parserDefinitionFactory;
        this.characterLexerFactory = characterLexerFactory;
        this.syntaxHighlighterFactoryProvider = syntaxHighlighterFactoryProvider;
        this.builderMenuBuilder = builderMenuBuilder;

        this.proxyFactory = new ProxyFactory();
        this.proxyFactory.setUseCache(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdeaLanguageAttachment get(@NotNull final ILanguage language) {
        IdeaLanguageAttachment obj = this.languages.get(language);
        if (obj == null) {
            obj = createLanguageAttachment(language);
            this.languages.put(language, obj);
            logger.info("Created a new IdeaLanguageAttachment for language {}.", language);
        } else {
            logger.info("Used cached IdeaLanguageAttachment for language {}.", language);
        }
        return obj;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IdeaLanguageImplAttachment get(@NotNull final ILanguageImpl implementation) {
        IdeaLanguageImplAttachment obj = this.implementations.get(implementation);
        if (obj == null) {
            obj = createLanguageImplAttachment(implementation);
            this.implementations.put(implementation, obj);
            logger.info("Created a new IdeaLanguageImplAttachment for language implementation {}.", implementation);
        } else {
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
        final ParserDefinition parserDefinition = createParserDefinition(fileType);
        final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory = createSyntaxHighlighterFactory();
        final Lexer characterLexer = createCharacterLexer(tokenTypeManager);
        final OldSpoofaxParser parser = createParser(dummyAstTokenType);

        return new IdeaLanguageAttachment(ideaLanguage,
                                          fileType,
                                          tokenTypeManager,
                                          dummyAstTokenType,
                                          parserDefinition,
                                          syntaxHighlighterFactory,
                                          characterLexer,
                                          parser);
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

        final Lexer lexer = createLexer(implementation, langAtt.tokenTypeManager);
        final DefaultActionGroup buildActionGroup = createAction(implementation);

        return new IdeaLanguageImplAttachment(lexer, buildActionGroup);
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
            return (SpoofaxIdeaLanguage) this.proxyFactory.create(new Class<?>[]{ILanguage.class},
                                                                  new Object[]{language});
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
            return (SpoofaxFileType) this.proxyFactory.create(new Class<?>[]{SpoofaxIdeaLanguage.class},
                                                              new Object[]{language});
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
    private final ParserDefinition createParserDefinition(@NotNull final SpoofaxFileType fileType) {
        return this.parserDefinitionFactory.create(fileType);
    }

    /**
     * Creates a new lexer.
     *
     * @param tokenTypeManager The token type manager.
     * @return The lexer.
     */
    @NotNull
    private final Lexer createCharacterLexer(@NotNull final SpoofaxTokenTypeManager tokenTypeManager) {
        return this.characterLexerFactory.create(tokenTypeManager);
    }

    /**
     * Creates a new lexer.
     *
     * @param language         The language.
     * @param tokenTypeManager The token type manager.
     * @return The lexer.
     */
    @NotNull
    private final Lexer createLexer(@NotNull final ILanguageImpl language,
                                    @NotNull final SpoofaxTokenTypeManager tokenTypeManager) {
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
     * @return The syntax highlighter factory.
     */
    @NotNull
    private final SpoofaxSyntaxHighlighterFactory createSyntaxHighlighterFactory() {
        return this.syntaxHighlighterFactoryProvider.get();
    }

    /**
     * Creates the builder menu action group.
     *
     * @param implementation The language implementation.
     * @return The action group.
     */
    @NotNull
    private final DefaultActionGroup createAction(@NotNull final ILanguageImpl implementation) {
        return this.builderMenuBuilder.build(implementation);
    }
}
