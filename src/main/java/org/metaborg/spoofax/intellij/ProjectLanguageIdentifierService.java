package org.metaborg.spoofax.intellij;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.build.dependency.IDependencyService;
import org.metaborg.core.language.*;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.settings.IProjectSettings;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * {@inheritDoc}
 */
@Singleton
public final class ProjectLanguageIdentifierService implements IProjectLanguageIdentifierService {

    @NotNull
    private final ILanguageService languageService;
    @NotNull
    private final IDependencyService dependencyService;

    @Inject
    private ProjectLanguageIdentifierService(@NotNull final ILanguageService languageService, @NotNull final IDependencyService dependencyService) {
        this.languageService = languageService;
        this.dependencyService = dependencyService;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public ILanguageImpl identify(@NotNull final ILanguage language, @Nullable final IProject project) {
        Iterable<ILanguageImpl> implementations = compileDependencies(project);
        return pickImplementation(language, implementations);
    }

    /**
     * Picks a single implementation for a language from a list of implementations.
     *
     * @param language The language.
     * @param implementations The list of all possible language implementations.
     * @return An implementation for the language; or <code>null</code> when not found.
     */
    @Nullable
    private ILanguageImpl pickImplementation(@NotNull final ILanguage language, @NotNull final Iterable<ILanguageImpl> implementations) {
        for(ILanguageImpl implementation : implementations) {
            if (implementation.belongsTo().equals(language))
                return implementation;
        }
        return null;
    }

    /**
     * Returns the compile dependencies of the project.
     *
     * @param project The project.
     * @return A sequence of compile dependency language implementations.
     */
    @NotNull
    private Iterable<ILanguageImpl> compileDependencies(IProject project) {
        if(project == null)
            return LanguageUtils.allActiveImpls(this.languageService);

        try {
            final Iterable<ILanguageComponent> dependencies = this.dependencyService.compileDependencies(project);
            return LanguageUtils.toImpls(dependencies);
        } catch(MetaborgException e) {
            return LanguageUtils.allActiveImpls(this.languageService);
        }
    }
}
