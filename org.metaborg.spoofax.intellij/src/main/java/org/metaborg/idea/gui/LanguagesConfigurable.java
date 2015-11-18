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

package org.metaborg.idea.gui;

import com.google.common.base.Preconditions;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.project.Project;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.project.IProject;

public abstract class LanguagesConfigurable extends BaseConfigurable {

    private final Project project;

    /**
     * Initializes a new instance of the {@link LanguagesConfigurable} class.
     *
     * @param project The IntelliJ project whose languages are configured.
     */
    protected LanguagesConfigurable(final Project project) {
        Preconditions.checkNotNull(project);

        this.project = project;
    }

    /**
     * Gets the IntelliJ project whose languages are configured.
     *
     * @return The IntelliJ project.
     */
    public Project project() { return this.project; }

    public void addLanguage() {
        // TODO
    }
    public void removeLanguage(final ILanguage language) {
        // TODO
    }
    public void editLanguage(final ILanguage language) {
        // TODO
    }
    public boolean canEditLanguage(final ILanguage language) {
        return true;
    }
    public boolean canRemoveLanguage(final ILanguage language) {
        return true;
    }
}
