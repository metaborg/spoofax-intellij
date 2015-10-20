package org.metaborg.spoofax.intellij.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions for working with IntelliJ actions.
 */
@Singleton
public final class ActionHelper {

    @NotNull private final IIntelliJResourceService resourceService;
    @NotNull private final ILanguageIdentifierService identifierService;

    @Inject
    private ActionHelper(@NotNull final IIntelliJResourceService resourceService,
                         @NotNull final ILanguageIdentifierService identifierService) {
        this.resourceService = resourceService;
        this.identifierService = identifierService;
    }

    /**
     * Gets a list of files currently selected.
     *
     * @param e The event arguments.
     * @return A list of files.
     */
    public List<FileObject> getActiveFiles(@NotNull final AnActionEvent e) {
        VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        ArrayList<FileObject> result = new ArrayList<>(files.length);
        if (files != null) {
            for (VirtualFile file : files) {
                if (file.isDirectory())
                    continue;
                result.add(this.resourceService.resolve(file));
            }
        }
        return result;
    }

//    /**
//     * Determines the language implementation of the active file.
//     *
//     * @param e The event arguments.
//     * @return The language implementation for the active file;
//     * or <code>null</code> when there are zero or multiple active files.
//     */
//    @Nullable
//    public ILanguageImpl getActiveFileLanguage(@NotNull final AnActionEvent e) {
//        List<FileObject> files = getActiveFiles(e);
//        if (files.size() != 1)
//            return null;
//        return this.identifierService.identify(files.get(0));
//    }

    /**
     * Determines whether all active files are of the specified language.
     *
     * @param e The event arguments.
     * @param language The language implementation to check.
     * @return <code>true</code> when all active files are of the specified language;
     * otherwise, <code>false</code>.
     */
    public boolean isActiveFileLanguage(@NotNull final AnActionEvent e, ILanguageImpl language) {
        List<FileObject> files = getActiveFiles(e);
        if (files.size() == 0)
            return false;
        for (FileObject file : files) {
            if (!this.identifierService.identify(file, language))
                return false;
        }
        return true;
    }

}
