package org.metaborg.spoofax.intellij.idea.project;

import com.google.inject.Inject;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageIdentifierService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.idea.languages.IIdeaLanguageManager;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
//
///**
// * Spoofax file editor manager listener.
// *
// * Listens to events from the file editor manager. For example, when a file is opened, closed, or the active
// * editor is changed.
// *
// * When a file is opened, the {@link #selectionChanged(FileEditorManagerEvent)} event is fired before
// * {@link #fileOpened(FileEditorManager, VirtualFile)}. When the active editor changes,
// * only the {@link #selectionChanged(FileEditorManagerEvent)} event is fired.
// *
// * This is a project-level listener, so it must be registered to the project's message bus. For example:
// * <pre>{@code
// * Project project;
// * SpoofaxFileEditorManagerListener listener;
// * project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, listener);
// * }</pre>
// */
//public final class SpoofaxFileEditorManagerListener implements FileEditorManagerListener {
//
//    @NotNull
//    private final ILanguageIdentifierService identifierService;
//    @NotNull
//    private final IIntelliJResourceService resourceService;
//    @NotNull
//    private final IIdeaLanguageManager languageManager;
//
//    @Inject
//    private SpoofaxFileEditorManagerListener(@NotNull final ILanguageIdentifierService identifierService,
//                                             @NotNull final IIntelliJResourceService resourceService,
//                                             @NotNull final IIdeaLanguageManager languageManager) {
//        this.identifierService = identifierService;
//        this.resourceService = resourceService;
//        this.languageManager = languageManager;
//    }
//
//    @Override
//    public void fileOpened(@NotNull final FileEditorManager fileEditorManager, @NotNull final VirtualFile virtualFile) {
//
//    }
//
//    @Override
//    public void fileClosed(@NotNull final FileEditorManager fileEditorManager, @NotNull final VirtualFile virtualFile) {
//
//    }
//
//    @Override
//    public void selectionChanged(@NotNull final FileEditorManagerEvent fileEditorManagerEvent) {
//        ILanguageImpl implementation = null;
//        VirtualFile newFile = fileEditorManagerEvent.getNewFile();
//        if (newFile != null) {
//            FileObject file = resourceService.resolve(newFile);
//            implementation = identifierService.identify(file);
//        }
//        languageManager.enableUI(implementation);
//    }
//}
