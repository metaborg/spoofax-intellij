package org.metaborg.spoofax.intellij.languages;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.lang.LanguageExtensionPoint;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.util.KeyedLazyInstanceEP;
import javassist.util.proxy.ProxyFactory;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.languages.syntax.*;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;
//
///**
// * Manages languages and file types for IntelliJ IDEA.
// */
//@Singleton
//public final class IdeaLanguageManager {
//
//    @InjectLogger
//    private Logger logger;
//    @NotNull
//    private final ProxyFactory proxyFactory;
//    @NotNull
//    private final ISpoofaxLexerAdapterFactory lexerFactory;
//    @NotNull
//    private final HashMap<ILanguage, IdeaObjects> objects = new HashMap<>();
//    // LanguageExtensionPoint
//    @NotNull
//    private final static String PARSER_DEFINITION_EXTENSION_POINT_NAME = "com.intellij.lang.parserDefinition";
//    @NotNull
//    private final static String SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION_POINT_NAME = "com.intellij.lang.syntaxHighlighterFactory";
//    @NotNull
//    private final static String FILE_TYPE_FACTORY_EXTENSION_POINT_NAME = "com.intellij.fileTypeFactory";
//    //private final static ExtensionPointName<ParserDefinition> PARSER_DEFINITION_EXTENSION_POINT_NAME = new ExtensionPointName<>("com.intellij.lang.parserDefinition");
//
//
//
//    /**
//     * Gets a collection of IDEA languages.
//     *
//     * @return A collection of IDEA languages.
//     */
//    @NotNull
//    public final Collection<SpoofaxIdeaLanguage> languages() {
//        return this.objects.values().stream().map(obj -> obj.ideaLanguage).collect(
//                Collectors.toList());
//    }
//
//    /**
//     * Gets a collection of file types.
//     *
//     * @return A collection of file types.
//     */
//    @NotNull
//    public final Collection<SpoofaxFileType> fileTypes() {
//        return this.objects.values().stream().map(obj -> obj.fileType).collect(
//                Collectors.toList());
//    }
//
//    @Inject
//    private IdeaLanguageManager(ISpoofaxLexerAdapterFactory lexerFactory) {
//        this.lexerFactory = lexerFactory;
//        this.proxyFactory = new ProxyFactory();
//        this.proxyFactory.setUseCache(false);
//    }
//
//
//    /**
//     * Registers a Spoofax language.
//     *
//     * @param language The language to register.
//     */
//    public final void register(@NotNull final ILanguage language) {
//        if (isRegistered(language))
//            throw new IllegalArgumentException("Language '" + language + "' is already registered.");
//
//        final SpoofaxIdeaLanguage ideaLanguage = createIdeaLanguage(language);
//        final SpoofaxFileType fileType = createFileType(ideaLanguage);
//        final SpoofaxTokenTypeManager tokenTypeManager = createTokenTypeManager(ideaLanguage);
//        final OldSpoofaxTokenType dummyAstTokenType = createDummyAstTokenType(ideaLanguage);
//        final SpoofaxLexer lexer = createLexer(language.activeImpl(), tokenTypeManager);
//        final OldSpoofaxParser parser = createParser(language.activeImpl());
//        final SpoofaxParserDefinition parserDefinition = createParserDefinition(fileType, lexer, parser);
//        final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory = createSyntaxHighlighterFactory(parserDefinition);
//        final SpoofaxLanguageFileTypeFactory fileTypeFactory = createFileTypeFactory(fileType);
//
//        final IdeaObjects obj = new IdeaObjects(ideaLanguage, fileType, tokenTypeManager, dummyAstTokenType, parserDefinition, lexer, parser, syntaxHighlighterFactory, fileTypeFactory);
//        this.objects.put(language, obj);
//
//
//
////        //Extensions.
////        //Object[] objs = Extensions.getExtensions(PARSER_DEFINITION_EXTENSION_POINT_NAME);
////        LanguageExtensionPoint<ParserDefinition> ext = new LanguageExtensionPoint<ParserDefinition>();
////        ext.language = ideaLanguage.getID();
////        ext.implementationClass = parserDefinition.getClass().getName();
//        Extensions.getRootArea().getExtensionPoint(PARSER_DEFINITION_EXTENSION_POINT_NAME).registerExtension(
//                new InstanceLanguageExtensionPoint(ideaLanguage, parserDefinition)
//        );
//
//        Extensions.getRootArea().getExtensionPoint(SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION_POINT_NAME).registerExtension(
//                new InstanceKeyedExtensionPoint(ideaLanguage, syntaxHighlighterFactory)
//        );
//        //FileTypeManager.getInstance().registerFileType(fileType, fileType.getExtensions());
//        FileTypeManagerEx.getInstanceEx().registerFileType(fileType);
//        for (String ext : fileType.getExtensions()) {
//            FileTypeManager.getInstance().associateExtension(fileType, ext);
//        }
////        Extensions.getRootArea().getExtensionPoint(FILE_TYPE_FACTORY_EXTENSION_POINT_NAME).registerExtension(
////                fileTypeFactory
////        );
//        //
//        //LanguageExtensionPoint[] extensions = (LanguageExtensionPoint[])objs;
////
////        //Extensions.getRootArea().
////        //ExtensionsArea rootArea = Extensions.getRootArea();
////        //ExtensionPoint<ParserDefinition> extensionPoint = (ExtensionPoint<ParserDefinition>)rootArea.getExtensionPoint(
////        //        PARSER_DEFINITION_EXTENSION_POINT_NAME);
////        //LanguageExtensionPoint<ParserDefinition> extensionPoint2 = (LanguageExtensionPoint<ParserDefinition>)extensionPoint;
////
////        //extensionPoint.registerExtension(parserDefinition);
////        Object[] objs = Extensions.getExtensions(SYNTAX_HIGHLIGHTER_FACTORY_EXTENSION_POINT_NAME);
////        System.out.println(objs);
//    }
//
//    /**
//     * Unregisters a Spoofax language.
//     *
//     * @param language The language to unregister.
//     * @return <code>true</code> when the language was unregistered;
//     * otherwise, <code>false</code> when the language was not found.
//     */
//    public final boolean unregister(@NotNull final ILanguage language) {
//        return this.objects.remove(language) != null;
//    }
//
//    /**
//     * Checks whether a language is registered.
//     *
//     * @param language The language to test.
//     * @return <code>true</code> when the language is registered;
//     * otherwise, <code>false</code>.
//     */
//    public final boolean isRegistered(@NotNull final ILanguage language) {
//        return this.objects.containsKey(language);
//    }
//
//    /**
//     * Gets the IDEA language for a Spoofax language.
//     *
//     * @param language The language.
//     * @return The associated IDEA language.
//     */
//    @NotNull
//    public final SpoofaxIdeaLanguage getIdeaLanguage(@NotNull final ILanguage language) {
//        if (!isRegistered(language))
//            throw new IllegalArgumentException("Language '" + language + "' is not registered.");
//
//        return this.objects.get(language).ideaLanguage;
//    }
//
//    /**
//     * Gets the file type for a Spoofax language.
//     *
//     * @param language The language.
//     * @return The associated file type.
//     */
//    @NotNull
//    public final SpoofaxFileType getFileType(@NotNull final ILanguage language) {
//        if (!isRegistered(language))
//            throw new IllegalArgumentException("Language '" + language + "' is not registered.");
//
//        return this.objects.get(language).fileType;
//    }
//
//    /**
//     * Gets the token type manager for a Spoofax language.
//     *
//     * @param language The language.
//     * @return The associated file type.
//     */
//    @NotNull
//    public final SpoofaxTokenTypeManager getTokenTypeManager(@NotNull final ILanguage language) {
//        if (!isRegistered(language))
//            throw new IllegalArgumentException("Language '" + language + "' is not registered.");
//
//        return this.objects.get(language).tokenTypeManager;
//    }
//
//    /**
//     * Gets the dummy AST token type for a Spoofax language.
//     *
//     * @param language The language.
//     * @return The associated token type.
//     */
//    @NotNull
//    public final OldSpoofaxTokenType getDummyAstTokenType(@NotNull final ILanguage language) {
//        if (!isRegistered(language))
//            throw new IllegalArgumentException("Language '" + language + "' is not registered.");
//
//        return this.objects.get(language).dummyAstTokenType;
//    }
//
//    /**
//     * Gets the parser definition for a Spoofax language.
//     *
//     * @param language The language.
//     * @return The associated parser definition.
//     */
//    @NotNull
//    public final SpoofaxParserDefinition getParserDefinition(@NotNull final ILanguage language) {
//        if (!isRegistered(language))
//            throw new IllegalArgumentException("Language '" + language + "' is not registered.");
//
//        return this.objects.get(language).parserDefinition;
//    }
//
//    /**
//     * Creates a new IDEA language for a Spoofax language.
//     *
//     * @param language The language.
//     * @return The created IDEA language.
//     */
//    @NotNull
//    private final SpoofaxIdeaLanguage createIdeaLanguage(@NotNull final ILanguage language) {
//        try {
//            this.proxyFactory.setSuperclass(SpoofaxIdeaLanguage.class);
//            return (SpoofaxIdeaLanguage)this.proxyFactory.create(new Class<?>[]{ ILanguage.class }, new Object[]{ language });
//        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//            this.logger.error("Unexpected unhandled exception.", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Creates a new file type for an IDEA language.
//     *
//     * @param language The IDEA language.
//     * @return The created file type.
//     */
//    @NotNull
//    private final SpoofaxFileType createFileType(@NotNull final SpoofaxIdeaLanguage language) {
//        try {
//            this.proxyFactory.setSuperclass(SpoofaxFileType.class);
//            return (SpoofaxFileType)this.proxyFactory.create(new Class<?>[]{ SpoofaxIdeaLanguage.class }, new Object[]{ language });
//        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
//            this.logger.error("Unexpected unhandled exception.", e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * Creates a new spoofax token type manager for an IDEA language.
//     *
//     * @param language The IDEA language.
//     * @return The created token type manager.
//     */
//    @NotNull
//    private final SpoofaxTokenTypeManager createTokenTypeManager(@NotNull final SpoofaxIdeaLanguage language) {
//        return new SpoofaxTokenTypeManager(language);
//    }
//
//    /**
//     * Creates a new dummy AST token type.
//     *
//     * @param language The IDEA language.
//     * @return The created dummy token type.
//     */
//    @NotNull
//    private final OldSpoofaxTokenType createDummyAstTokenType(@NotNull final SpoofaxIdeaLanguage language) {
//        return new OldSpoofaxTokenType("DUMMY", language);
//    }
//
//    /**
//     * Creates a new parser definition.
//     * @param fileType The language file type.
//     * @return The created parser definition.
//     */
//    @NotNull
//    private final SpoofaxParserDefinition createParserDefinition(@NotNull final SpoofaxFileType fileType, @NotNull final SpoofaxLexer lexer, @NotNull final OldSpoofaxParser parser) {
//        //Proxy.getProxyClass()
////        try {
////            this.proxyFactory.setSuperclass(SpoofaxParserDefinition.class);
////            return this.proxyFactory.createClass();
////        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
////            this.logger.error("Unexpected unhandled exception.", e);
////            throw new RuntimeException(e);
////        }
//
//        //SpoofaxParserDefinition
//        return new SpoofaxParserDefinition(fileType, lexer, parser);
//    }
//
//    /**
//     * Creates a new lexer.
//     * @param language The language.
//     * @return The lexer.
//     */
//    @NotNull
//    private final SpoofaxLexer createLexer(@NotNull final ILanguageImpl language, @NotNull final SpoofaxTokenTypeManager tokenTypeManager) {
//        return this.lexerFactory.create(language, tokenTypeManager);
//    }
//
//    /**
//     * Creates a new parser.
//     * @param language The language.
//     * @return The parser.
//     */
//    @NotNull
//    private final OldSpoofaxParser createParser(@NotNull final ILanguageImpl language) {
//        return new OldSpoofaxParser(this);
//    }
//
//    /**
//     * Creates a new syntax highlighter factory.
//     * @param parserDefinition The parser definition.
//     * @return The syntax highlighter factory.
//     */
//    @NotNull
//    private final SpoofaxSyntaxHighlighterFactory createSyntaxHighlighterFactory(@NotNull final SpoofaxParserDefinition parserDefinition) {
//        return new SpoofaxSyntaxHighlighterFactory(parserDefinition);
//    }
//
//    /**
//     * Creates a new file type factory.
//     * @param fileType The file type.
//     * @return The file type factory.
//     */
//    @NotNull
//    private final SpoofaxLanguageFileTypeFactory createFileTypeFactory(@NotNull final SpoofaxFileType fileType) {
//        return new SpoofaxLanguageFileTypeFactory(fileType);
//    }
//
//    /**
//     * Stores the IntelliJ IDEA objects that are associated with a particular language.
//     */
//    private final class IdeaObjects {
//        @NotNull public final SpoofaxIdeaLanguage ideaLanguage;
//        @NotNull public final SpoofaxFileType fileType;
//        @NotNull public final SpoofaxTokenTypeManager tokenTypeManager;
//        @NotNull public final OldSpoofaxTokenType dummyAstTokenType;
//        @NotNull public final SpoofaxParserDefinition parserDefinition;
//        @NotNull public final SpoofaxLexer lexer;
//        @NotNull public final OldSpoofaxParser parser;
//        @NotNull public final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory;
//        @NotNull public final FileTypeFactory fileTypeFactory;
//
//        public IdeaObjects(@NotNull final SpoofaxIdeaLanguage ideaLanguage, @NotNull final SpoofaxFileType fileType, @NotNull final SpoofaxTokenTypeManager tokenTypeManager, @NotNull final OldSpoofaxTokenType dummyAstTokenType, @NotNull final SpoofaxParserDefinition parserDefinition, @NotNull final SpoofaxLexer lexer, @NotNull final OldSpoofaxParser parser, @NotNull final SpoofaxSyntaxHighlighterFactory syntaxHighlighterFactory, @NotNull final FileTypeFactory fileTypeFactory) {
//            this.ideaLanguage = ideaLanguage;
//            this.fileType = fileType;
//            this.tokenTypeManager = tokenTypeManager;
//            this.dummyAstTokenType = dummyAstTokenType;
//            this.parserDefinition = parserDefinition;
//            this.lexer = lexer;
//            this.parser = parser;
//            this.syntaxHighlighterFactory = syntaxHighlighterFactory;
//            this.fileTypeFactory = fileTypeFactory;
//        }
//    }
//
//}
