package org.metaborg.spoofax.intellij;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.factories.ISpoofaxFileEditorManagerListenerFactory;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.idea.project.SpoofaxFileEditorManagerListener;
import org.metaborg.spoofax.intellij.languages.LanguageManager;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.metaborg.spoofax.intellij.serialization.SpoofaxProjectService;
import org.slf4j.Logger;

// Remove this class.
public final class SpoofaxProjectComponent implements ProjectComponent {

    @NotNull
    private ISpoofaxFileEditorManagerListenerFactory fileEditorManagerListenerFactory;
    @NotNull
    private final Project project;
    @InjectLogger
    private Logger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxProjectComponent(@NotNull Project project) {
        this.project = project;
        IdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private final void inject(@NotNull final ISpoofaxFileEditorManagerListenerFactory fileEditorManagerListenerFactory) {
        this.fileEditorManagerListenerFactory = fileEditorManagerListenerFactory;
    }

    @Override
    public final void projectOpened() {
        SpoofaxProjectService.getInstance(this.project).getState().setMyName("Project name!");
    }

    @Override
    public final void projectClosed() {

    }

    @Override
    public final void initComponent() {

        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                                                    this.fileEditorManagerListenerFactory.create());

    }

    @Override
    public final void disposeComponent() {

    }

    @NotNull
    @Override
    public final String getComponentName() {
        return this.getClass().getName();
    }
}
