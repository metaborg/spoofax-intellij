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

package org.metaborg.intellij.idea.languages;

// TODO: Add to Metaborg Core

import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;

import javax.annotation.*;
import java.util.*;

/**
 * Retrieves the languages for files in projects.
 */
public interface ILanguageProjectService {

    /**
     * Gets the language implementations that are active in the specified project.
     *
     * @param project The project.
     * @return The active language implementations.
     */
    Iterable<ILanguageImpl> activeImpls(@Nullable IProject project);

    /**
     * Gets the language implementation or dialect for the specified project/file combination.
     * <p>
     * The less arguments are <code>null</code>, the better chance of finding
     * a single candidate is.
     *
     * @param language The language.
     * @param project  The project; or <code>null</code> when not known.
     * @param file     The file; or <code>null</code> when not known.
     * @return The language implementation or dialect of the file,
     * or the active language implementation for the language in the project;
     * or <code>null</code> when the language implementation or dialect
     * could not be determined.
     */
    @Nullable
    LanguageDialect getImpl(@Nullable ILanguage language, @Nullable IProject project, @Nullable FileObject file);

    /**
     * Gets the language implementation or dialect for the specified project/file.
     * <p>
     * The less arguments are <code>null</code>, the better chance of finding
     * a single candidate is.
     * <p>
     * An exception is thrown when more than one language implementation applies.
     *
     * @param project The project.
     * @param file    The file.
     * @return One of the specified languages that is the language implementation or dialect
     * of the file; or <code>null</code> when none of the specified languages is a language
     * implementation or dialect of the file.
     */
    @Nullable
    LanguageDialect getImpl(
            @Nullable Iterable<? extends ILanguageImpl> languages,
            @Nullable IProject project,
            @Nullable FileObject file);

    /**
     * Gets the candidate language implementation or dialects for the specified project/file
     * combination.
     * <p>
     * The less arguments are <code>null</code>, the more specific the resulting list
     * of candidates is.
     *
     * @param languages The language implementations from which to pick; or <code>null</code>
     *                  to pick from all loaded language implementations.
     * @param project   The project; or <code>null</code> when the project is not known.
     * @param file      The file; or <code>null</code> when the file is not known.
     * @return The language implementations or dialects that can apply to the specified project/file
     * combination.
     */
    Set<LanguageDialect> getCandidateImpls(
            @Nullable Iterable<? extends ILanguageImpl> languages,
            @Nullable IProject project,
            @Nullable FileObject file);

    /**
     * Gets the candidate language implementation or dialects for the specified project/file
     * combination.
     * <p>
     * The less arguments are <code>null</code>, the more specific the resulting list
     * of candidates is.
     *
     * @param language The language from which to pick; or <code>null</code>
     *                 to pick from all loaded languages.
     * @param project  The project; or <code>null</code> when the project is not known.
     * @param file     The file; or <code>null</code> when the file is not known.
     * @return The language implementations or dialects that can apply to the specified project/file
     * combination.
     */
    Set<LanguageDialect> getCandidateImpls(
            @Nullable ILanguage language,
            @Nullable IProject project,
            @Nullable FileObject file);

}
