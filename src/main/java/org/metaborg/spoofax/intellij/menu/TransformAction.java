package org.metaborg.spoofax.intellij.menu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguageImpl;

import javax.swing.*;

/**
 * A transformation action from a builder menu.
 */
public final class TransformAction extends AnActionWithId {

    @NotNull
    private final ILanguageImpl implementation;

    /**
     * Initializes a new instance of the {@link TransformAction} class.
     *
     * @param id The ID of the action.
     * @param implementation The language implementation.
     */
    public TransformAction(@NotNull String id, @NotNull ILanguageImpl implementation) {
        this(id, implementation, (String)null, (String)null, (Icon)null);
    }

    /**
     * Initializes a new instance of the {@link TransformAction} class.
     *
     * @param id The ID of the action.
     * @param icon The icon of the action; or <code>null</code>.
     */
    public TransformAction(@NotNull String id, @NotNull ILanguageImpl implementation, Icon icon) {
        this(id, implementation, (String)null, (String)null, icon);
    }

    /**
     * Initializes a new instance of the {@link TransformAction} class.
     *
     * @param id The ID of the action.
     * @param text The text of the action; or <code>null</code>.
     */
    public TransformAction(@NotNull String id, @NotNull ILanguageImpl implementation, @Nullable String text) {
        this(id, implementation, text, (String)null, (Icon)null);
    }

    /**
     * Initializes a new instance of the {@link TransformAction} class.
     *
     * @param id The ID of the action.
     * @param text The text of the action; or <code>null</code>.
     * @param description The description of the action; or <code>null</code>.
     * @param icon The icon of the action; or <code>null</code>.
     */
    public TransformAction(@NotNull String id, @NotNull ILanguageImpl implementation, @Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(id, text, description, icon);
        this.implementation = implementation;
    }

    @Override
    public void actionPerformed(final AnActionEvent e) {
        VirtualFile[] files = e.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        System.out.println(files);
    }
}
