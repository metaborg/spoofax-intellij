/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
