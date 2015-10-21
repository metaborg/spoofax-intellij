package org.metaborg.spoofax.intellij.menu;

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.core.menu.TransformAction;

import java.util.List;

/**
 * Executes a transformation action on resources.
 */
public interface IResourceTransformer {

    /**
     * Executes the specified action.
     *
     * @param action      The action to execute.
     * @param language    The language implementation.
     * @param activeFiles The active files.
     */
    boolean execute(@NotNull final TransformAction action,
                    @NotNull final ILanguageImpl language,
                    @NotNull final List<FileObject> activeFiles)
            throws MetaborgException;
}
