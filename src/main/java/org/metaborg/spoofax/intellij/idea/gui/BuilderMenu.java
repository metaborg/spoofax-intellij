package org.metaborg.spoofax.intellij.idea.gui;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.menu.IAction;
import org.metaborg.core.menu.IMenu;
import org.metaborg.core.menu.IMenuItem;
import org.metaborg.core.menu.IMenuService;
import org.metaborg.spoofax.intellij.IdentifierUtils;
import org.metaborg.spoofax.intellij.idea.languages.IdeaLanguageImplAttachment;

/**
 * A builder menu.
 */
public final class BuilderMenu implements IDynamicAction {

    @NotNull
    private final IMenuService menuService;
    @NotNull
    private final ILanguageImpl implementation;
    @NotNull
    private final DefaultActionGroup menuGroup;
    private final String MENU_FIRST;
    @NotNull
    private final AnAction menuFirst;

    @Inject
    private BuilderMenu(@Assisted @NotNull final ILanguageImpl implementation,
                        @NotNull final IMenuService menuService) {
        this.implementation = implementation;
        this.menuService = menuService;

        Iterable<IMenuItem> menuItems = this.menuService.menuItems(this.implementation);
        for (IMenuItem item : menuItems) {
            // TODO: createMenuItem(item)
        }

        this.MENU_FIRST = IdentifierUtils.create("SPOOFAX_FIRST_" + this.implementation.belongsTo().name() + "_");

        this.menuGroup = new DefaultActionGroup(this.implementation.belongsTo().name(), true);
        this.menuFirst = new AnAction("tEST!") {
            @Override
            public void actionPerformed(final AnActionEvent anActionEvent) {
                System.out.println("Test! >>" + implementation.id().toString());
            }
        };
        this.menuGroup.add(this.menuFirst, new Constraints(Anchor.LAST, "after"));
    }

    @Override
    public void enable(@NotNull final ActionManager manager) {
        DefaultActionGroup mainMenu = (DefaultActionGroup) manager.getAction("MainMenu");
        manager.registerAction(this.MENU_FIRST, this.menuFirst);
        mainMenu.add(this.menuGroup);
    }

    @Override
    public void disable(@NotNull final ActionManager manager) {
        DefaultActionGroup mainMenu = (DefaultActionGroup) manager.getAction("MainMenu");
        mainMenu.remove(this.menuGroup);
        manager.unregisterAction(this.MENU_FIRST);
    }

    @NotNull
    private AnAction createMenuItem(@NotNull final IMenuItem item) {
        // TODO
        throw new RuntimeException();
    }
}
