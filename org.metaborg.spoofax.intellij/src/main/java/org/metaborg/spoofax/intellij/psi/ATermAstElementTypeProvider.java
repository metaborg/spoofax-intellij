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

package org.metaborg.spoofax.intellij.psi;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.psi.tree.IElementType;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.source.ISourceRegion;
import org.metaborg.core.style.ICategorizerService;
import org.metaborg.core.style.ICategory;
import org.metaborg.core.style.IRegionCategory;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.spoofax.core.style.TokenCategory;
import org.metaborg.spoofax.core.syntax.JSGLRSourceRegionFactory;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;
import org.spoofax.interpreter.terms.IStrategoTerm;
import org.spoofax.jsglr.client.imploder.IToken;
import org.spoofax.jsglr.client.imploder.ITokenizer;
import org.spoofax.jsglr.client.imploder.ImploderAttachment;

import javax.annotation.Nullable;

public final class ATermAstElementTypeProvider implements IAstElementTypeProvider<IStrategoTerm> {

    private final ParseResult<IStrategoTerm> parseResult;
    private final ILanguageImpl language;
    private final SpoofaxTokenTypeManager tokenTypesManager;
    private final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizerService;
    private final Iterable<IRegionCategory<IStrategoTerm>> categories;

    @Inject
    public ATermAstElementTypeProvider(@Assisted final ILanguageImpl language, @Assisted final ParseResult<IStrategoTerm> parseResult,
                                       @Assisted final SpoofaxTokenTypeManager tokenTypesManager, final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizerService) {
        this.language = language;
        this.parseResult = parseResult;
        this.tokenTypesManager = tokenTypesManager;
        this.categorizerService = categorizerService;
        this.categories = this.categorizerService.categorize(language, parseResult);
    }

    @Override
    public IElementType getElementType(IStrategoTerm term) {
        final ICategory category = getCategory(term);
        IElementType elementType = null;
        if (category instanceof TokenCategory) {
            switch (category.name()) {
                case "TK_IDENTIFIER":
                    elementType = this.tokenTypesManager.getIdentifierType();
                    break;
                default:
                    break;
            }
        }
        if (elementType == null)
            elementType = this.tokenTypesManager.getElementType();

        return elementType;
    }

    @Nullable
    private ICategory getCategory(IStrategoTerm term) {
        final ImploderAttachment rootImploderAttachment = ImploderAttachment.get(term);
        final IToken token = rootImploderAttachment.getLeftToken();
        final ISourceRegion region = JSGLRSourceRegionFactory.fromToken(token);
        for (IRegionCategory<IStrategoTerm> category : this.categories) {
            if ((category.fragment() != null && category.fragment().equals(term)) || category.region().equals(region)) {
//            if (category.region().equals(region)) {
                return category.category();
            }
        }
        return null;
    }
}
