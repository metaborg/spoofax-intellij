package org.metaborg.spoofax.intellij.menu;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

import javax.annotation.Nullable;

/**
 * Action group for a language builder.
 */
public final class BuilderActionGroup extends DefaultActionGroup {

    @NotNull
    private final ILanguageImpl implementation;
    @NotNull
    private final IIntelliJResourceService resourceService;
    @NotNull
    private final ILanguageIdentifierService identifierService;

    /**
     * Initializes a new instance of the {@link BuilderActionGroup} class.
     *
     * @param implementation The implementation to respond to.
     */
    @Inject
    private BuilderActionGroup(@Assisted @NotNull final ILanguageImpl implementation, @NotNull final IIntelliJResourceService resourceService, @NotNull final ILanguageIdentifierService identifierService) {
        super(implementation.belongsTo().name(), true);
        this.implementation = implementation;
        this.resourceService = resourceService;
        this.identifierService = identifierService;
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        ILanguageImpl implementation = getActiveFileLanguage(e);
        e.getPresentation().setVisible(implementation == this.implementation);
        super.update(e);
    }

    /**
     * Determines the language implementation of the active file.
     *
     * @param e The event arguments.
     * @return The language implementation for the active file; or <code>null</code>.
     */
    @Nullable
    private ILanguageImpl getActiveFileLanguage(@NotNull final AnActionEvent e) {
        VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (files == null || files.length != 1 || files[0].isDirectory())
            return null;
        FileObject file = this.resourceService.resolve(files[0]);
        return identifierService.identify(file);
    }
}
