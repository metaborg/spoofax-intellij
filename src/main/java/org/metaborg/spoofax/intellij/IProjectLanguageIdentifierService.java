package org.metaborg.spoofax.intellij;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.project.IProject;

import javax.annotation.Nullable;

/**
 * Interface for identifying the implementation of a language.
 */
public interface IProjectLanguageIdentifierService {

    /**
     * Attempts to identify the active implementation of the given language.
     *
     * @param language The language whose implementation to identify.
     * @param project The project in whose context the language must be identified; or <code>null</code>.
     * @return The identified language implementation; or <code>null</code> when not found.
     */
    @Nullable
    ILanguageImpl identify(@NotNull ILanguage language, @Nullable IProject project);

}
