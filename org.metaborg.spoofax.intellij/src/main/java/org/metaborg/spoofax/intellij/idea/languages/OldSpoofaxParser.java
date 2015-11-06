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
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;

// NOTE: This class will eventually be replaced.
@Singleton
public final class OldSpoofaxParser implements PsiParser {

    @NotNull
    private final ILanguage language;
    @NotNull
    private final SpoofaxTokenTypeManager tokenTypesManager;

    @Inject
    public OldSpoofaxParser(@Assisted @NotNull final ILanguage language,
                            @Assisted @NotNull final SpoofaxTokenTypeManager tokenTypesManager) {
        this.language = language;
        this.tokenTypesManager = tokenTypesManager;
    }

    @NotNull
    @Override
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        PsiFile file = builder.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY);
        VirtualFile vf = file.getVirtualFile();
        // TODO: Use the VirtualFile to determine the ILanguageImpl to use to parse.
        SpoofaxIdeaLanguage language = (SpoofaxIdeaLanguage) root.getLanguage();

        PsiBuilder.Marker m = builder.mark();
        parseFile(builder);
        //m.done(this.dummyAstTokenType);
        m.done(root);
        return builder.getTreeBuilt();
    }

    private void parseFile(PsiBuilder builder) {
        PsiBuilder.Marker m = builder.mark();
        while (!builder.eof()) {
            builder.advanceLexer();
        }
        m.done(this.tokenTypesManager.getDummySpoofaxTokenType());
    }

}
