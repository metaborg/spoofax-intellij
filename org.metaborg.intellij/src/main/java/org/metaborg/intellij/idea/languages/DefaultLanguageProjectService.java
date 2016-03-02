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

import com.google.common.collect.*;
import com.google.inject.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.*;
import org.metaborg.core.build.dependency.*;
import org.metaborg.core.language.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.logging.LoggerUtils;
import org.metaborg.meta.core.project.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.util.*;

// TODO: Retry with all active languages if not found in project languages,
// just like `getImpl(IProject, FileObject)`

/**
 * Default implementation of {@link ILanguageProjectService}.
 */
public final class DefaultLanguageProjectService implements ILanguageProjectService {

    private final ILanguageIdentifierService identifierService;
    private final IDependencyService dependencyService;
    private final ILanguageService languageService;
    private final ILanguageSpecService languageSpecService;
    @InjectLogger
    private ILogger logger;

    @Inject
    public DefaultLanguageProjectService(
            final ILanguageIdentifierService identifierService,
            final IDependencyService dependencyService,
            final ILanguageService languageService,
            final ILanguageSpecService languageSpecService) {
        this.identifierService = identifierService;
        this.dependencyService = dependencyService;
        this.languageService = languageService;
        this.languageSpecService = languageSpecService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<ILanguageImpl> activeImpls(@Nullable final IProject project) {
        try {
//            @Nullable final ILanguageSpec languageSpec = this.languageSpecService.get(project);
//            // FIXME: Do something when languageSpec == null.
            final Iterable<ILanguageComponent> dependencies = this.dependencyService.compileDeps(project);
            return LanguageUtils.toImpls(dependencies);
        } catch (final MetaborgException e) {
            // There is nothing we can do about this exception.
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public LanguageDialect getImpl(
            @Nullable final ILanguage language,
            @Nullable final IProject project,
            @Nullable final FileObject file) {
        @Nullable final Iterable<? extends ILanguageImpl> impls = language != null ? language.impls() : null;
        final Set<LanguageDialect> candidates = getCandidateImpls(impls, project, file);
        if (candidates.size() > 1) {
            throw LoggerUtils.exception(this.logger, IllegalStateException.class,
                    "For language {} more than one candidate implementation found for file {} in project {}: {}",
                    language, file, project, candidates);
        }
        return !candidates.isEmpty() ? candidates.iterator().next() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public LanguageDialect getImpl(
            @Nullable final Iterable<? extends ILanguageImpl> languages,
            @Nullable final IProject project,
            @Nullable final FileObject file) {
        final Set<LanguageDialect> candidates = getCandidateImpls(languages, project, file);
        if (candidates.size() > 1)
            throw new IllegalStateException(this.logger.format(
                    "From {} more than one candidate implementation found for file {} in project {}: {}",
                    languages, file, project, candidates
            ));
        return !candidates.isEmpty() ? candidates.iterator().next() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<LanguageDialect> getCandidateImpls(
            @Nullable final Iterable<? extends ILanguageImpl> languages,
            @Nullable final IProject project,
            @Nullable final FileObject file) {
        if (languages == null)
            return getCandidateImpls(this.languageService.getAllImpls(), project, file);

        final Set<LanguageDialect> candidates = new HashSet<>();
        if (file != null) {
            // Find all implementations that the file identifies to.
            for (final ILanguageImpl impl : languages) {
                if (this.identifierService.identify(file, impl)) {
                    candidates.add(new LanguageDialect(impl, null));
                }
            }
        } else if (project != null) {
            // Find all implementations that the project supports.
            final Set<ILanguageImpl> input = Sets.newHashSet(activeImpls(project));
            input.retainAll(Lists.newArrayList(languages));
            for (final ILanguageImpl impl : input) {
                candidates.add(new LanguageDialect(impl, null));
            }
        } else {
            // Find all implementations.
            for (final ILanguageImpl impl : languages) {
                candidates.add(new LanguageDialect(impl, null));
            }
        }
        return candidates;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<LanguageDialect> getCandidateImpls(
            @Nullable final ILanguage language,
            @Nullable final IProject project,
            @Nullable final FileObject file) {
        return getCandidateImpls(language != null ? language.impls() : Collections.EMPTY_LIST, project, file);
    }
}
