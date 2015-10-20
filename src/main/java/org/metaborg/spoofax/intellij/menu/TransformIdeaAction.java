package org.metaborg.spoofax.intellij.menu;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.context.ContextException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.menu.IMenuService;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.processing.parse.IParseResultRequester;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.core.transform.ITransformer;
import org.metaborg.core.transform.ITransformerGoal;
import org.metaborg.core.transform.NestedNamedGoal;
import org.metaborg.core.transform.TransformerException;
import org.metaborg.spoofax.core.menu.TransformAction;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.util.concurrent.IClosableLock;
import org.slf4j.Logger;

import javax.swing.*;
import java.util.List;

/**
 * A transformation action from a builder menu.
 */
public final class TransformIdeaAction extends AnActionWithId {

    @InjectLogger
    private Logger logger;
    @NotNull
    private final ILanguageImpl language;
    @NotNull
    private final TransformAction action;
    @NotNull
    private final ActionHelper actionHelper;
    @NotNull
    private final IResourceTransformer transformer;

    @Inject
    private TransformIdeaAction(@Assisted @NotNull final String id, @Assisted @NotNull final ILanguageImpl language, @Assisted @NotNull final TransformAction action, @NotNull final ActionHelper actionHelper, @NotNull final IResourceTransformer transformer) {
        super(id, action.name(), (String)null, (Icon)null);
        this.language = language;
        this.action = action;
        this.actionHelper = actionHelper;
        this.transformer = transformer;
    }

    @Override
    public void actionPerformed(final AnActionEvent e) {
        List<FileObject> files = this.actionHelper.getActiveFiles(e);
        // TODO: Wrap in write ??
        try {
            this.transformer.execute(this.action, this.language, files);
        } catch (MetaborgException ex) {
            this.logger.error("An exception occurred: {}", ex);
        }
    }
}
