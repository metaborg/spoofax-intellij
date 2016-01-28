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

package org.metaborg.intellij.idea.gui;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.project.Project;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageProjectService;
import org.metaborg.core.language.LanguageDialect;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides languages.
 */
public final class DefaultLanguageListItemsProvider implements ILanguageListItemsProvider {

    private final Project project;
    private final ILanguageProjectService languageProjectService;

    /**
     * Initializes a new instance of the {@link DefaultLanguageListItemsProvider} class.
     *
     * @param project The IDEA project for which to list the languages.
     */
    @Inject
    public DefaultLanguageListItemsProvider(@Assisted final Project project, final ILanguageProjectService languageProjectService) {
        Preconditions.checkNotNull(project);
        Preconditions.checkNotNull(languageProjectService);

        this.project = project;
        this.languageProjectService = languageProjectService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<ILanguage> all() {
        // TODO: Get only languages per IntelliJ project (i.e. workspace)
        Set<LanguageDialect> impls = this.languageProjectService.getCandidateImpls((ILanguage)null, null, null);
        return impls.stream().map(x -> x.dialectOrBaseLanguage().belongsTo()).collect(Collectors.toList());
    }
}
