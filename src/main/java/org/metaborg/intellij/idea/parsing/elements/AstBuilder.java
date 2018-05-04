/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.metaborg.intellij.idea.parsing.elements;

import com.google.common.base.Preconditions;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.spoofax.interpreter.terms.IStrategoConstructor;
import org.spoofax.interpreter.terms.IStrategoList;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;
import org.spoofax.terms.Term;

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
     * @param tokenTypesManager
     *            The token types manager.
     */
    public AstBuilder(final ILanguageImpl language,
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
     * @param parseResult
     *            The parse result.
     * @param root
     *            The root element type.
     * @param builder
     *            The PSI builder.
     * @return The PSI AST tree root.
     */
    public ASTNode build(final ISpoofaxParseUnit parseResult, final IElementType root, final PsiBuilder builder) {

        final ATermAstElementTypeProvider elementTypeProvider =
            this.elementTypeProviderFactory.create(this.language, parseResult, this.tokenTypesManager);

        final PsiBuilder.Marker m = builder.mark();
        if(parseResult.valid()) {
            buildTermIterative(builder, parseResult.ast(), elementTypeProvider);
        } else {
            // We have no parse result. Therefore,
            // parse a single element for all tokens.
            final PsiBuilder.Marker m2 = builder.mark();
            while(!builder.eof()) {
                builder.advanceLexer();
            }
            m2.done(this.tokenTypesManager.getElementType());
        }
        m.done(root);

        return builder.getTreeBuilt();
    }

    /**
     * Checks whether the term is amb or not.
     *
     * @param term term to check
     * @return true if term is amb
     */
    private boolean isAmbNode(final IStrategoTerm term) {
        IStrategoConstructor ctor = Term.tryGetConstructor(term);
        return ctor != null && ctor.getArity() == 1 && Term.termAt(term, 0).isList() && ctor.getName().equals("amb");
    }

    /**
     * Builds the AST for the specified term, iteratively.
     *
     * @param root
     *            The root term.
     */
    private void buildTermIterative(final PsiBuilder builder, final IStrategoTerm root,
                                    final ATermAstElementTypeProvider elementTypeProvider) {
        final Stack<TermTask> tasks = new Stack<>();
        tasks.push(new TermTask(root));

        while(!tasks.empty()) {
            final TermTask task = tasks.pop();
            final IStrategoTerm term = task.term();
            @Nullable PsiBuilder.Marker marker = task.marker();
            if(marker == null) {
                // Start
                marker = buildTermStart(builder, term);
                tasks.push(new TermTask(term, marker));

                if (isAmbNode(term)) {
                    final IStrategoList list = Term.termAt(term, 0);
                    tasks.push(new TermTask(list.head()));
                } else {
                    final IStrategoTerm[] subterms = term.getAllSubterms();
                    for(int i = subterms.length - 1; i >= 0; i--) {
                        tasks.push(new TermTask(subterms[i]));
                    }
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
     * @param term
     *            The term.
     * @return The resulting marker.
     */
    private PsiBuilder.Marker buildTermStart(final PsiBuilder builder, final IStrategoTerm term) {
        moveToStart(builder, term);
        return builder.mark();
    }

    /**
     * Builds the end of a term.
     *
     * @param term
     *            The term.
     * @param marker
     *            The marker.
     */
    private void buildTermEnd(final PsiBuilder builder, final IStrategoTerm term, final PsiBuilder.Marker marker,
                              final ATermAstElementTypeProvider elementTypeProvider) {
        final IElementType elementType = elementTypeProvider.getElementType(term);

        moveToEnd(builder, term);
        marker.done(elementType);
    }

    /**
     * Moves to the start of a term.
     *
     * @param term
     *            The term.
     */
    private void moveToStart(final PsiBuilder builder, final IStrategoTerm term) {
        final ImploderAttachment imploderAttachment = ImploderAttachment.get(term);
        final int start = imploderAttachment.getLeftToken().getStartOffset();
        moveTo(builder, start);
    }

    /**
     * Moves to the end of a term.
     *
     * @param term
     *            The term.
     */
    private void moveToEnd(final PsiBuilder builder, final IStrategoTerm term) {
        final ImploderAttachment imploderAttachment = ImploderAttachment.get(term);
        final int end = imploderAttachment.getRightToken().getEndOffset() + 1;
        moveTo(builder, end);
    }

    /**
     * Move the builder to the specified offset.
     *
     * @param offset
     *            The target offset.
     */
    private void moveTo(final PsiBuilder builder, final int offset) {
        // We assume the builder to be _before_ the target offset.
        while(builder.getCurrentOffset() < offset) {
            builder.advanceLexer();
        }
        // We assume the lexer to have a 1 character step increments,
        // so we can't overshoot our target.
        assert builder.getCurrentOffset() == offset;
    }

    private static class TermTask {
        private final IStrategoTerm term;
        @Nullable private final PsiBuilder.Marker marker;

        public IStrategoTerm term() {
            return this.term;
        }

        @Nullable public PsiBuilder.Marker marker() {
            return this.marker;
        }

        public TermTask(final IStrategoTerm term) {
            this(term, null);
        }

        public TermTask(final IStrategoTerm term, @Nullable final PsiBuilder.Marker marker) {
            this.term = term;
            this.marker = marker;
        }
    }
}
