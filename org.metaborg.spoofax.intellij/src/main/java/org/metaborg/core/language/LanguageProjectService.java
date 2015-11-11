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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.build.dependency.IDependencyService;
import org.metaborg.core.project.IProject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Set;

// TODO: Retry with all active languages if not found in project languages,
// just like `getImpl(IProject, FileObject)`

/**
 * Implementation of {@link ILanguageProjectService}.
 */
public final class LanguageProjectService implements ILanguageProjectService {

    private final ILanguageIdentifierService identifierService;
    private final IDependencyService dependencyService;
    private final ILanguageService languageService;

    @Inject
    /* package private */ LanguageProjectService(final ILanguageIdentifierService identifierService, final IDependencyService dependencyService, final ILanguageService languageService) {
        this.identifierService = identifierService;
        this.dependencyService = dependencyService;
        this.languageService = languageService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<ILanguageImpl> activeImpls(final IProject project) {
        try {
            final Iterable<ILanguageComponent> dependencies = this.dependencyService.compileDependencies(project);
            return LanguageUtils.toImpls(dependencies);
        } catch (MetaborgException e) {
            // There is nothing we can do about this exception.
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ILanguageImpl getImpl(final IProject project, final ILanguage language) {
        Iterable<ILanguageImpl> impls = activeImpls(project);
        Set<ILanguageImpl> candidates = getImplsBelongToLanguage(language, impls);
        if (candidates.size() > 1)
            throw new IllegalStateException("More than one candidate implementation found for the specified language.");
        return candidates.size() != 0 ? candidates.iterator().next() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public LanguageDialect getImpl(final IProject project, final FileObject file) {
        LanguageDialect result = getImpl(project, file, activeImpls(project));
        if(result == null) {
            // Try with all active languages if identification with dependencies fails
            result = getImpl(project, file, LanguageUtils.allActiveImpls(this.languageService));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public LanguageDialect getImpl(final IProject project, final ILanguage language, @Nullable final FileObject file) {
        if (file != null) {
            LanguageDialect result = getImpl(project, file);
            if (result.dialectOrBaseLanguage().belongsTo().equals(language))
                return result;
            else
                return null;
        }
        else {
            ILanguageImpl impl = getImpl(project, language);
            if (impl != null)
                return new LanguageDialect(impl, null);
            else
                return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ILanguageImpl getImpl(
            final IProject project, final Iterable<? extends ILanguageImpl> languages) {
        Set<ILanguageImpl> candidates = Sets.newHashSet(activeImpls(project));
        candidates.retainAll(Lists.newArrayList(languages));
        if (candidates.size() > 1)
            throw new IllegalStateException("More than one candidate implementation found for the specified language.");
        return candidates.size() != 0 ? candidates.iterator().next() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public LanguageDialect getImpl(
            final IProject project, final FileObject file, final Iterable<? extends ILanguageImpl> languages) {
        IdentifiedResource identifiedResource = this.identifierService.identifyToResource(file, languages);
        if (identifiedResource == null)
            return null;
        return new LanguageDialect(identifiedResource.language, identifiedResource.dialect);
    }

    /**
     * Returns only those implementations that belong to the specified language.
     *
     * @param language The language.
     * @param impls The implementations.
     * @return A set of implementations, which may be empty.
     */
    private Set<ILanguageImpl> getImplsBelongToLanguage(final ILanguage language, final Iterable<? extends ILanguageImpl> impls) {
        final Set<ILanguageImpl> identifiedImpls = Sets.newLinkedHashSet();
        for(ILanguageImpl impl : impls) {
            if (impl.belongsTo().equals(language)) {
                identifiedImpls.add(impl);
            }
        }
        return identifiedImpls;
    }
}
