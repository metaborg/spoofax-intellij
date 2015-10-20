package org.metaborg.spoofax.intellij.factories;

import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxTokenTypeManager;
import org.metaborg.spoofax.intellij.idea.project.SpoofaxFileEditorManagerListener;

/**
 * Factory for the Spoofac file editor manager listener.
 */
public interface ISpoofaxFileEditorManagerListenerFactory {

    /**
     * Creates a new file editor manager listener.
     *
     * @return The created listener.
     */
    @NotNull
    SpoofaxFileEditorManagerListener create();

}
