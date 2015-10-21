package org.metaborg.spoofax.intellij.menu;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.core.menu.TransformAction;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

import javax.swing.*;
import java.util.List;

//import org.jetbrains.annotations.NotNull;

/**
 * A transformation action from a builder menu.
 */
public final class TransformationAction extends AnActionWithId {

    @NotNull
    private final ILanguageImpl language;
    @NotNull
    private final TransformAction action;
    @NotNull
    private final ActionHelper actionHelper;
    @NotNull
    private final IResourceTransformer transformer;
    @InjectLogger
    private Logger logger;

    @Inject
    private TransformationAction(@Assisted @NotNull final String id,
                                 @Assisted @NotNull final ILanguageImpl language,
                                 @Assisted @NotNull final TransformAction action,
                                 @NotNull final ActionHelper actionHelper,
                                 @NotNull final IResourceTransformer transformer) {
        super(id, action.name(), (String) null, (Icon) null);
        this.language = language;
        this.action = action;
        this.actionHelper = actionHelper;
        this.transformer = transformer;
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        List<FileObject> files = this.actionHelper.getActiveFiles(e);
        WriteCommandAction.runWriteCommandAction(project, () -> {
            try {
                this.transformer.execute(this.action, this.language, files);
            } catch (MetaborgException ex) {
                this.logger.error("An exception occurred: {}", ex);
            }
        });
    }

}