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

package org.metaborg.spoofax.intellij.languages;

import com.intellij.psi.PsiElement;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;

import javax.annotation.Nullable;

/**
 * IDEA language identifier service.
 */
public interface IIdeaLanguageIdentifierService extends ILanguageIdentifierService {

    /**
     * Determines the language implementation to use for the file to which
     * the specified {@link PsiElement} belongs.
     *
     * @param language The language of the file.
     * @param element The PSI element.
     * @return The language implementation.
     */
    @Nullable
    ILanguageImpl identify(ILanguage language, PsiElement element);

}
