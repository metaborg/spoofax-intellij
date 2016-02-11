/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.idea.parsing.elements;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.psi.tree.*;
import org.metaborg.core.language.*;
import org.metaborg.core.source.*;
import org.metaborg.core.style.*;
import org.metaborg.core.syntax.*;
import org.metaborg.spoofax.core.style.*;
import org.metaborg.spoofax.core.syntax.*;
import org.spoofax.interpreter.terms.*;
import org.spoofax.jsglr.client.imploder.*;

import javax.annotation.*;

public final class ATermAstElementTypeProvider implements IAstElementTypeProvider<IStrategoTerm> {

    private final ParseResult<IStrategoTerm> parseResult;
    private final ILanguageImpl language;
    private final SpoofaxTokenTypeManager tokenTypesManager;
    private final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizerService;
    private final Iterable<IRegionCategory<IStrategoTerm>> categories;

    @Inject
    public ATermAstElementTypeProvider(
            @Assisted final ILanguageImpl language,
            @Assisted final ParseResult<IStrategoTerm> parseResult,
            @Assisted final SpoofaxTokenTypeManager tokenTypesManager,
            final ICategorizerService<IStrategoTerm, IStrategoTerm> categorizerService) {
        this.language = language;
        this.parseResult = parseResult;
        this.tokenTypesManager = tokenTypesManager;
        this.categorizerService = categorizerService;
        this.categories = this.categorizerService.categorize(language, parseResult);
    }

    @Override
    public IElementType getElementType(final IStrategoTerm term) {
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
    private ICategory getCategory(final IStrategoTerm term) {
        final ImploderAttachment rootImploderAttachment = ImploderAttachment.get(term);
        final IToken token = rootImploderAttachment.getLeftToken();
        final ISourceRegion region = JSGLRSourceRegionFactory.fromToken(token);
        for (final IRegionCategory<IStrategoTerm> category : this.categories) {
            if ((category.fragment() != null && category.fragment().equals(term)) || category.region().equals(region)) {
                return category.category();
            }
        }
        return null;
    }
}
