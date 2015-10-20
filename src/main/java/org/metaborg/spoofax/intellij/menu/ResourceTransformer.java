package org.metaborg.spoofax.intellij.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.context.ContextException;
import org.metaborg.core.context.IContext;
import org.metaborg.core.context.IContextService;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.menu.IMenuService;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.processing.parse.IParseResultRequester;
import org.metaborg.core.source.ISourceTextService;
import org.metaborg.core.syntax.ParseResult;
import org.metaborg.core.transform.ITransformer;
import org.metaborg.core.transform.ITransformerGoal;
import org.metaborg.core.transform.TransformerException;
import org.metaborg.spoofax.core.menu.TransformAction;
import org.metaborg.spoofax.intellij.StringFormatter;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.util.concurrent.IClosableLock;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Executes a transformation action on resources.
 */
public class ResourceTransformer<P, A, T> implements IResourceTransformer {

    @InjectLogger
    private Logger logger;
    @NotNull
    private final IContextService contextService;
    @NotNull
    private final ISourceTextService sourceTextService;
    @NotNull
    private final ILanguageIdentifierService identifierService;
    @NotNull
    private final IParseResultRequester<P> parseResultRequester;
    @NotNull
    private final IAnalysisResultRequester<P, A> analysisResultRequester;
    @NotNull
    private final ITransformer<P, A, T> transformer;

    protected ResourceTransformer(
                    @NotNull
            final IContextService contextService,
                    @NotNull
            final ISourceTextService sourceTextService,
                    @NotNull
            final ILanguageIdentifierService identifierService,
                    @NotNull
            final IParseResultRequester<P> parseResultRequester,
                    @NotNull
            final IAnalysisResultRequester<P, A> analysisResultRequester,
                    @NotNull
            final ITransformer<P, A, T> transformer
    ) {
        this.contextService = contextService;
        this.sourceTextService = sourceTextService;
        this.identifierService = identifierService;
        this.parseResultRequester = parseResultRequester;
        this.analysisResultRequester = analysisResultRequester;
        this.transformer = transformer;
    }

    /**
     * Executes the specified action.
     *
     * @param action The action to execute.
     * @param language The language implementation.
     * @param activeFiles The active files.
     */
    @Override
    public boolean execute(@NotNull final TransformAction action, @NotNull final ILanguageImpl language, @NotNull final List<FileObject> activeFiles) throws
            MetaborgException {
//        ILanguageImpl language = this.actionHelper.getActiveFileLanguage(e);
//        if (language != this.language)
//            return;
//
//        List<FileObject> files = this.actionHelper.getActiveFiles(e);
//        if (files.size() != 1)
//            return;
//        FileObject file = files.get(0);

        List<TransformResource> resources = new ArrayList<>();
        for (FileObject file : activeFiles) {
            if (!this.identifierService.identify(file, language))
                continue;
            try {
                final String text = this.sourceTextService.text(file);
                resources.add(new TransformResource(file, text));
            } catch (IOException e) {
                this.logger.error("Cannot transform {}; skipped. Exception while retrieving text: {}", file, e);
                continue;
            }
        }
        for (TransformResource resource : resources) {
            try {
                transform(resource, action.goal, language, action.flags.parsed);
            } catch (ContextException | TransformerException e) {
                this.logger.error("Transformation failed for {}: {}", resource.resource(), e);
                // TODO: Display error!
                return false;
            }
        }

        return true;
    }

    private void transform(TransformResource resource, ITransformerGoal goal, ILanguageImpl language, boolean parsed) throws ContextException,
            TransformerException {
        final IContext context = this.contextService.get(resource.resource(), language);
        if (parsed) {
            final ParseResult<P> result = this.parseResultRequester.request(resource.resource(), language, resource.text()).toBlocking().single();
            this.transformer.transform(result, context, goal);
        } else {
            final AnalysisFileResult<P, A> result = this.analysisResultRequester.request(resource.resource(), context, resource.text()).toBlocking().single();
            try (IClosableLock lock = context.read()) {
                transformer.transform(result, context, goal);
            }
        }
    }
}
