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
import com.google.inject.Singleton;
import com.google.inject.assistedinject.Assisted;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.psi.tree.IElementType;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.syntax.IParserConfiguration;
import org.metaborg.core.syntax.ISyntaxService;
import org.metaborg.core.syntax.ParseException;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;

import javax.annotation.Nullable;
import java.util.Stack;

/**
 * Parser for Spoofax languages.
 */
@Singleton
public final class SpoofaxParser implements PsiParser {

    @NotNull
    private final ILanguage language;
    @NotNull
    private final SpoofaxTokenTypeManager tokenTypesManager;
    @NotNull
    private final ILanguageIdentifierService identifierService;
    @NotNull
    private final IIntelliJResourceService resourceService;
    @NotNull
    private final ISyntaxService<IStrategoTerm> syntaxService;
    @NotNull
    private final IParserConfiguration parserConfiguration;

    @Inject
    public SpoofaxParser(@Assisted @NotNull final ILanguage language,
                         @Assisted @NotNull final SpoofaxTokenTypeManager tokenTypesManager,
                         @NotNull final ILanguageIdentifierService identifierService,
                         @NotNull final IIntelliJResourceService resourceService,
                         @NotNull final ISyntaxService<IStrategoTerm> syntaxService,
                         @NotNull final IParserConfiguration parserConfiguration) {
        this.language = language;
        this.tokenTypesManager = tokenTypesManager;
        this.identifierService = identifierService;
        this.resourceService = resourceService;
        this.syntaxService = syntaxService;
        this.parserConfiguration = parserConfiguration;
    }

    @NotNull
    @Override
    public ASTNode parse(@NotNull final IElementType root, @NotNull final PsiBuilder builder) {
        FileObject resource = getResource(builder);
        ILanguageImpl languageImpl = getLanguageImpl(resource, root);
        String input = builder.getOriginalText().toString();
        ParseResult<IStrategoTerm> result = parseAll(resource, languageImpl, input);

        SpoofaxAstBuilder astBuilder = new SpoofaxAstBuilder(result, root, builder, this.tokenTypesManager);
        return astBuilder.build();
    }

    /**
     * Parses the whole buffer.
     *
     * @return The parse result.
     */
    private ParseResult<IStrategoTerm> parseAll(final FileObject resource, final ILanguageImpl languageImpl, final String input) {
        ParseResult<IStrategoTerm> result;
        try {
            result = this.syntaxService.parse(input, resource, languageImpl, this.parserConfiguration);
        } catch (ParseException e) {
            throw new MetaborgRuntimeException("Unhandled exception", e);
        }
        return result;
    }

    /**
     * Determines the resource being parsed.
     *
     * @param builder The PSI builder.
     * @return The {@FileObject} of the parsed resource; or <code>null</code>.
     */
    @Nullable
    private FileObject getResource(@NotNull final PsiBuilder builder) {
        PsiFile file = builder.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY);
        FileObject fileObject = null;
        if (file != null) {
            VirtualFile virtualFile = file.getVirtualFile();
            if (virtualFile != null) {
                fileObject = this.resourceService.resolve(virtualFile);
            }
        }
        return fileObject;
    }

    /**
     * Determines the language implementation to use to parse this file.
     *
     * @param resource The file object of the file being parsed.
     * @param root The root element.
     * @return The language implementation to use.
     */
    @NotNull
    private ILanguageImpl getLanguageImpl(@Nullable final FileObject resource, @NotNull final IElementType root) {
        ILanguageImpl languageImpl = null;
        if (resource != null) {
            languageImpl = this.identifierService.identify(resource);
        }
        if (languageImpl == null) {
            SpoofaxIdeaLanguage language = (SpoofaxIdeaLanguage) root.getLanguage();
            // TODO: Pick a language implementation when the file/implementation is not known?
            throw new UnsupportedOperationException();
        }
        return languageImpl;
    }

    /**
     * Builds a PSI AST.
     */
    private static class SpoofaxAstBuilder {

        @NotNull private final SpoofaxTokenTypeManager tokenTypesManager;
        @Nullable private final ParseResult<IStrategoTerm> result;
        @NotNull private final PsiBuilder builder;
        @NotNull private final IElementType root;

        /**
         * Initializes a new instance of the {@link SpoofaxAstBuilder} class.
         *
         * @param result The parse result.
         * @param root The root element type.
         * @param builder The PSI builder.
         * @param tokenTypesManager The token types manager.
         */
        public SpoofaxAstBuilder(@Nullable final ParseResult<IStrategoTerm> result, @NotNull final IElementType root, @NotNull final PsiBuilder builder, @NotNull final SpoofaxTokenTypeManager tokenTypesManager) {
            this.result = result;
            this.root = root;
            this.builder = builder;
            this.tokenTypesManager = tokenTypesManager;
        }

        /**
         * Builds the PSI AST from the parse result.
         *
         * @return The PSI AST tree root.
         */
        @NotNull
        public ASTNode build() {
            PsiBuilder.Marker m = builder.mark();
            if (this.result != null && this.result.result != null) {
                buildTermIterative(this.result.result);
            } else {
                // We have no parse result. Therefore,
                // parse a single element for all tokens.
                PsiBuilder.Marker m2 = builder.mark();
                while (!builder.eof()) {
                    builder.advanceLexer();
                }
                m2.done(this.tokenTypesManager.getDummySpoofaxTokenType());
            }
            m.done(this.root);

            return builder.getTreeBuilt();
        }

        /**
         * Builds the AST for the specified term, iteratively.
         *
         * @param root The root term.
         */
        private void buildTermIterative(@NotNull final IStrategoTerm root) {
            Stack<TermTask> tasks = new Stack<>();
            tasks.push(new TermTask(root));

            while (!tasks.empty()) {
                TermTask task = tasks.pop();
                IStrategoTerm term = task.term;
                PsiBuilder.Marker marker = task.marker;
                if (marker == null) {
                    // Start
                    marker = buildTermStart(term);
                    tasks.push(new TermTask(term, marker));

                    IStrategoTerm[] subterms = term.getAllSubterms();
                    for (int i = subterms.length - 1; i >= 0; i--) {
                        tasks.push(new TermTask(subterms[i]));
                    }
                } else {
                    // End
                    buildTermEnd(term, marker);
                }
            }
        }

        /**
         * Builds the AST for the specified term, recursively.
         *
         * @param term The term.
         */
        private void buildTermRecursive(@NotNull final IStrategoTerm term) {
            PsiBuilder.Marker marker = buildTermStart(term);

            IStrategoTerm[] subterms = term.getAllSubterms();
            for (int i = 0; i < subterms.length; i++) {
                // Recurse
                buildTermRecursive(subterms[i]);
            }

            buildTermEnd(term, marker);
        }

        private PsiBuilder.Marker buildTermStart(IStrategoTerm term) {
            moveToStart(term);
            PsiBuilder.Marker marker = builder.mark();
            return marker;
        }

        private void buildTermEnd(IStrategoTerm term, PsiBuilder.Marker marker) {
            // TODO: Pick an element type!
            IElementType elementType = this.tokenTypesManager.getDummySpoofaxTokenType();

            moveToEnd(term);
            marker.done(elementType);
        }

        private void moveToStart(IStrategoTerm term) {
            final ImploderAttachment imploderAttachment = ImploderAttachment.get(term);
            int start = imploderAttachment.getLeftToken().getStartOffset();
            moveTo(start);
        }

        private void moveToEnd(IStrategoTerm term) {
            final ImploderAttachment imploderAttachment = ImploderAttachment.get(term);
            int end = imploderAttachment.getRightToken().getEndOffset() + 1;
            moveTo(end);
        }

        /**
         * Move the builder to the specified offset.
         *
         * @param offset The target offset.
         */
        private void moveTo(int offset) {
            // We assume the builder to be _before_ the target offset.
            while (this.builder.getCurrentOffset() < offset) {
                this.builder.advanceLexer();
            }
            // We assume the lexer to have a 1 character step increments,
            // so we can't overshoot our target.
            assert this.builder.getCurrentOffset() == offset;
        }

        private static class TermTask {
            public final IStrategoTerm term;
            @Nullable
            public final PsiBuilder.Marker marker;

            public TermTask(IStrategoTerm term, PsiBuilder.Marker marker) {
                this.term = term;
                this.marker = marker;
            }
            public TermTask(IStrategoTerm term) {
                this(term, null);
            }
        }
    }
}
