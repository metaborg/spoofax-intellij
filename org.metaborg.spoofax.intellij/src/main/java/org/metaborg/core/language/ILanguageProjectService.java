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

package org.metaborg.core.language;

// TODO: Add to Metaborg Core

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.project.IProject;

import javax.annotation.Nullable;

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
    Iterable<ILanguageImpl> activeImpls(IProject project);

    /**
     * Gets the language implementation that is active in the specified project
     * for the specified language.
     *
     * @param project The project.
     * @param language The language.
     * @return The active language implementation for the specified language;
     * or <code>null</code> when the specified language has no active language
     * implementation.
     */
    @Nullable
    ILanguageImpl getImpl(IProject project, ILanguage language);

    /**
     * Gets the language implementation or dialect for the specified file.
     *
     * @param project The project.
     * @param file The file.
     * @return The language implementation or dialect of the file;
     * or <code>null</code> when the language implementation or dialect
     * of the specified file could not be determined.
     */
    @Nullable
    LanguageDialect getImpl(IProject project, FileObject file);

    /**
     * Gets the language implementation or dialect for the specified file.
     *
     * @param project The project.
     * @param language The language.
     * @param file The file.
     * @return The language implementation or dialect of the file,
     * or the active language implementation for the language in the project;
     * or <code>null</code> when the language implementation or dialect
     * could not be determined.
     */
    @Nullable
    LanguageDialect getImpl(IProject project, ILanguage language, @Nullable FileObject file);

    /**
     * Gets the language implementation that is active in the specified project
     * from the specified iterable.
     *
     * An exception is thrown when more than one language implementation applies.
     *
     * @param project The project.
     * @param languages The possible languages.
     * @return One of the specified languages that is the active language implementation;
     * or <code>null</code> when none of the specified languages is an active language
     * implementation.
     */
    @Nullable
    ILanguageImpl getImpl(IProject project, Iterable<? extends ILanguageImpl> languages);

    /**
     * Gets the language implementation or dialect for the specified file.
     *
     * An exception is thrown when more than one language implementation applies.
     *
     * @param project The project.
     * @param file The file.
     * @return One of the specified languages that is the language implementation or dialect
     * of the file; or <code>null</code> when none of the specified languages is a language
     * implementation or dialect of the file.
     */
    @Nullable
    LanguageDialect getImpl(IProject project, FileObject file, Iterable<? extends ILanguageImpl> languages);

}
