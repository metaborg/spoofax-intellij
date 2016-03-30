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

import org.metaborg.intellij.idea.filetypes.MetaborgLanguageFileType;
import org.metaborg.spoofax.core.unit.ISpoofaxAnalyzeUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxParseUnit;
import org.metaborg.spoofax.core.unit.ISpoofaxTransformUnit;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.util.Key;
import com.intellij.psi.FileViewProvider;

/**
 * Metaborg source file PSI element.
 */
public final class MetaborgFile extends PsiFileBase {

    public static final Key<ISpoofaxParseUnit> PARSE_RESULT_KEY = new Key<>("PARSE_RESULT_KEY");
    public static final Key<ISpoofaxAnalyzeUnit> ANALYSIS_FILE_RESULT_KEY = new Key<>(
            "ANALYSIS_FILE_RESULT_KEY");
    public static final Key<ISpoofaxTransformUnit<?>> ANALYSIS_RESULT_KEY = new Key<>(
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