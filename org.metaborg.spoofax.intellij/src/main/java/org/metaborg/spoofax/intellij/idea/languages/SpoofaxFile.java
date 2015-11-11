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

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.analysis.AnalysisResult;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.spoofax.intellij.idea.vfs.SpoofaxFileType;
import org.spoofax.interpreter.terms.IStrategoTerm;

/**
 * A Spoofax source file.
 */
public final class SpoofaxFile extends PsiFileBase {

    public static final Key<ParseResult<IStrategoTerm>> PARSE_RESULT_KEY = new Key<>("PARSE_RESULT_KEY");
    public static final Key<AnalysisFileResult<IStrategoTerm, IStrategoTerm>> ANALYSIS_FILE_RESULT_KEY = new Key<>("ANALYSIS_FILE_RESULT_KEY");
    public static final Key<AnalysisResult<IStrategoTerm, IStrategoTerm>> ANALYSIS_RESULT_KEY = new Key<>("ANALYSIS_RESULT_KEY");

    @NotNull
    private final SpoofaxFileType fileType;

    /**
     * Initializes a new instance of the {@link SpoofaxFile} class.
     *
     * @param viewProvider The file view provider.
     * @param fileType     The file type.
     */
    public SpoofaxFile(@NotNull final FileViewProvider viewProvider, @NotNull final SpoofaxFileType fileType) {
        super(viewProvider, fileType.getLanguage());
        this.fileType = fileType;
    }

    /**
     * Gets the file type of the file.
     *
     * @return The file type.
     */
    @NotNull
    public FileType getFileType() {
        return this.fileType;
    }

}