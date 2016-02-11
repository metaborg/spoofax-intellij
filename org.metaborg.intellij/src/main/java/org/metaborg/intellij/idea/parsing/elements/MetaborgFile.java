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

import com.intellij.extapi.psi.*;
import com.intellij.openapi.util.*;
import com.intellij.psi.*;
import org.metaborg.core.analysis.*;
import org.metaborg.core.syntax.*;
import org.metaborg.intellij.idea.filetypes.*;
import org.spoofax.interpreter.terms.*;

/**
 * Metaborg source file PSI element.
 */
public final class MetaborgFile extends PsiFileBase {

    public static final Key<ParseResult<IStrategoTerm>> PARSE_RESULT_KEY = new Key<>("PARSE_RESULT_KEY");
    public static final Key<AnalysisFileResult<IStrategoTerm, IStrategoTerm>> ANALYSIS_FILE_RESULT_KEY = new Key<>(
            "ANALYSIS_FILE_RESULT_KEY");
    public static final Key<AnalysisResult<IStrategoTerm, IStrategoTerm>> ANALYSIS_RESULT_KEY = new Key<>(
            "ANALYSIS_RESULT_KEY");

    private final MetaborgLanguageFileType fileType;

    /**
     * Initializes a new instance of the {@link MetaborgFile} class.
     *
     * @param viewProvider The file view provider.
     * @param fileType     The file type.
     */
    public MetaborgFile(final FileViewProvider viewProvider, final MetaborgLanguageFileType fileType) {
        super(viewProvider, fileType.getLanguage());
        this.fileType = fileType;
    }

    /**
     * Gets the file type of the file.
     *
     * @return The file type.
     */
    @Override
    public MetaborgLanguageFileType getFileType() {
        return this.fileType;
    }

}