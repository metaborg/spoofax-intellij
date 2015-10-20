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
    private final ActionHelper actionHelper;

    /**
     * Initializes a new instance of the {@link BuilderActionGroup} class.
     *
     * @param implementation The implementation to respond to.
     */
    @Inject
    private BuilderActionGroup(@Assisted @NotNull final ILanguageImpl implementation, @NotNull final ActionHelper actionHelper) {
        super(implementation.belongsTo().name(), true);
        this.implementation = implementation;
        this.actionHelper = actionHelper;
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        boolean visible = this.actionHelper.isActiveFileLanguage(e, this.implementation);
        e.getPresentation().setVisible(visible);
        super.update(e);
    }

}
