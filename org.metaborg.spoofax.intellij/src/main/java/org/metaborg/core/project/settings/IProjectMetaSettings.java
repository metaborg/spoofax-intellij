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

package org.metaborg.core.project.settings;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.spoofax.core.project.settings.Format;

/**
 * Meta settings for a project.
 */
public interface IProjectMetaSettings extends IProjectSettings {

    /**
     * Gets the location of the project root.
     *
     * @return The project root location.
     */
    FileObject location();

    /**
     * Gets a sequence of languages whose errors are ignored.
     *
     * @return An iterable of pardoned languages.
     */
    Iterable<String> pardonedLanguages();

    /**
     * Gets the project artifact format.
     *
     * @return A member of the {@link Format} enumeration.
     */
    Format format();

    /**
     * Gets SDF arguments.
     *
     * @return An iterable of SDF arguments.
     */
    Iterable<String> sdfArgs();

    /**
     * Gets the Stratego arguments.
     *
     * @return The stratego arguments.
     */
    Iterable<String> strategoArgs();

    /**
     * Gets the external def.
     *
     * @return The external def.
     */
    String externalDef();

    /**
     * Gets the external JAR.
     *
     * @return The external JAR.
     */
    String externalJar();

    /**
     * Gets the external JAR flags.
     *
     * @return The external JAR flags.
     */
    String externalJarFlags();

    /**
     * Gets the Stratego name.
     *
     * @return The Stratego name.
     */
    String strategoName();

    /**
     * Gets the Java name.
     *
     * @return The Java name.
     */
    String javaName();

    /**
     * Gets the package name.
     *
     * @return The package name.
     */
    String packageName();

    /**
     * Gets the package path.
     *
     * @return The package path.
     */
    String packagePath();

    /**
     * Gets the generated source directory.
     *
     * @return The generated source directory.
     */
    FileObject getGeneratedSourceDirectory();

    /**
     * Gets the output directory.
     *
     * @return The output directory.
     */
    FileObject getOutputDirectory();

    /**
     * Gets the icons directory.
     *
     * @return The icons directory.
     */
    FileObject getIconsDirectory();

    /**
     * Gets the lib directory.
     *
     * @return The lib directory.
     */
    FileObject getLibDirectory();

    /**
     * Gets the syntax directory.
     *
     * @return The syntax directory.
     */
    FileObject getSyntaxDirectory();

    /**
     * Gets the editor directory.
     *
     * @return The editor directory.
     */
    FileObject getEditorDirectory();

    /**
     * Gets the Java directory.
     *
     * @return The Java directory.
     */
    FileObject getJavaDirectory();

    /**
     * Gets the Java trans directory.
     *
     * @return The Java trans directory.
     */
    FileObject getJavaTransDirectory();

    /**
     * Gets the generated syntax directory.
     *
     * @return The generated syntax directory.
     */
    FileObject getGeneratedSyntaxDirectory();

    /**
     * Gets the trans directory.
     *
     * @return The trans directory.
     */
    FileObject getTransDirectory();

    /**
     * Gets the cache directory.
     *
     * @return The cache directory.
     */
    FileObject getCacheDirectory();

    /**
     * Gets the main ESV file.
     *
     * @return The main ESV file.
     */
    FileObject getMainESVFile();
}
