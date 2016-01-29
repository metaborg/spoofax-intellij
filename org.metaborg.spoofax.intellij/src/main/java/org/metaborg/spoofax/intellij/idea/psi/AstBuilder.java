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

package org.metaborg.spoofax.intellij.idea.psi;

import com.google.common.base.Preconditions;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.spoofax.intellij.factories.IATermAstElementTypeProviderFactory;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;

import javax.annotation.Nullable;
import java.util.Stack;

/**
 * Builds an IntelliJ AST from a ATerm AST.
 */
public final class AstBuilder {

    private final ILanguageImpl language;
    private final SpoofaxTokenTypeManager tokenTypesManager;
    private final IATermAstElementTypeProviderFactory elementTypeProviderFactory;

    /**
     * Initializes a new instance of the {@link AstBuilder} class.
     *
     * @param tokenTypesManager The token types manager.
     */
    public AstBuilder(
            final ILanguageImpl language,
            final IATermAstElementTypeProviderFactory elementTypeProviderFactory,
            final SpoofaxTokenTypeManager tokenTypesManager) {
        Preconditions.checkNotNull(language);
        Preconditions.checkNotNull(elementTypeProviderFactory);
        Preconditions.checkNotNull(tokenTypesManager);

        this.language = language;
        this.tokenTypesManager = tokenTypesManager;
        this.elementTypeProviderFactory = elementTypeProviderFactory;
    }

    /**
     * Builds the PSI AST from the parse result.
     *
     * @param parseResult The parse result.
     * @param root        The root element type.
     * @param builder     The PSI builder.
     * @return The PSI AST tree root.
     */
    public ASTNode build(
            final ParseResult<IStrategoTerm> parseResult,
            final IElementType root,
            final PsiBuilder builder) {
        Preconditions.checkNotNull(parseResult);
        Preconditions.checkNotNull(root);
        Preconditions.checkNotNull(builder);

        final ATermAstElementTypeProvider elementTypeProvider = this.elementTypeProviderFactory.create(
                this.language,
                parseResult,
                this.tokenTypesManager
        );

        final PsiBuilder.Marker m = builder.mark();
        if (parseResult != null && parseResult.result != null) {
            buildTermIterative(builder, parseResult.result, elementTypeProvider);
        } else {
            // We have no parse result. Therefore,
            // parse a single element for all tokens.
            final PsiBuilder.Marker m2 = builder.mark();
            while (!builder.eof()) {
                builder.advanceLexer();
            }
            m2.done(this.tokenTypesManager.getElementType());
        }
        m.done(root);

        return builder.getTreeBuilt();
    }

    /**
     * Builds the AST for the specified term, iteratively.
     *
     * @param root The root term.
     */
    private void buildTermIterative(
            final PsiBuilder builder,
            final IStrategoTerm root,
            final ATermAstElementTypeProvider elementTypeProvider) {
        final Stack<TermTask> tasks = new Stack<>();
        tasks.push(new TermTask(root));

        while (!tasks.empty()) {
            final TermTask task = tasks.pop();
            final IStrategoTerm term = task.term;
            PsiBuilder.Marker marker = task.marker;
            if (marker == null) {
                // Start
                marker = buildTermStart(builder, term);
                tasks.push(new TermTask(term, marker));

                final IStrategoTerm[] subterms = term.getAllSubterms();
                for (int i = subterms.length - 1; i >= 0; i--) {
                    tasks.push(new TermTask(subterms[i]));
                }
            } else {
                // End
                buildTermEnd(builder, term, marker, elementTypeProvider);
            }
        }
    }

    /**
     * Builds the start of a term.
     *
     * @param term The term.
     * @return The resulting marker.
     */
    private PsiBuilder.Marker buildTermStart(final PsiBuilder builder, final IStrategoTerm term) {
        moveToStart(builder, term);
        return builder.mark();
    }

    /**
     * Builds the end of a term.
     *
     * @param term   The term.
     * @param marker The marker.
     */
    private void buildTermEnd(
            final PsiBuilder builder,
            final IStrategoTerm term,
            final PsiBuilder.Marker marker,
            final ATermAstElementTypeProvider elementTypeProvider) {
        final IElementType elementType = elementTypeProvider.getElementType(term);

        moveToEnd(builder, term);
        marker.done(elementType);
    }

    /**
     * Moves to the start of a term.
     *
     * @param term The term.
     */
    private void moveToStart(final PsiBuilder builder, final IStrategoTerm term) {
        final ImploderAttachment imploderAttachment = ImploderAttachment.get(term);
        final int start = imploderAttachment.getLeftToken().getStartOffset();
        moveTo(builder, start);
    }

    /**
     * Moves to the end of a term.
     *
     * @param term The term.
     */
    private void moveToEnd(final PsiBuilder builder, final IStrategoTerm term) {
        final ImploderAttachment imploderAttachment = ImploderAttachment.get(term);
        final int end = imploderAttachment.getRightToken().getEndOffset() + 1;
        moveTo(builder, end);
    }

    /**
     * Move the builder to the specified offset.
     *
     * @param offset The target offset.
     */
    private void moveTo(final PsiBuilder builder, final int offset) {
        // We assume the builder to be _before_ the target offset.
        while (builder.getCurrentOffset() < offset) {
            builder.advanceLexer();
        }
        // We assume the lexer to have a 1 character step increments,
        // so we can't overshoot our target.
        assert builder.getCurrentOffset() == offset;
    }

    /**
     * Builds the AST for the specified term, recursively.
     *
     * @param term The term.
     */
    private void buildTermRecursive(
            final PsiBuilder builder,
            final IStrategoTerm term,
            final ATermAstElementTypeProvider elementTypeProvider) {
        final PsiBuilder.Marker marker = buildTermStart(builder, term);

        final IStrategoTerm[] subterms = term.getAllSubterms();
        for (final IStrategoTerm subterm : subterms) {
            // Recurse
            buildTermRecursive(builder, subterm, elementTypeProvider);
        }

        buildTermEnd(builder, term, marker, elementTypeProvider);
    }

    private static class TermTask {
        private final IStrategoTerm term;
        @Nullable
        private final PsiBuilder.Marker marker;

        public IStrategoTerm term() { return this.term;}

        @Nullable
        public PsiBuilder.Marker marker() { return this.marker;}

        public TermTask(final IStrategoTerm term) {
            this(term, null);
        }

        public TermTask(final IStrategoTerm term, final PsiBuilder.Marker marker) {
            this.term = term;
            this.marker = marker;
        }
    }
}
