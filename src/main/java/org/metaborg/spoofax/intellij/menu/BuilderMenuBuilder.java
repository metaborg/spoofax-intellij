package org.metaborg.spoofax.intellij.menu;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.actionSystem.*;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgRuntimeException;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.menu.*;
import org.metaborg.core.menu.Separator;
import org.metaborg.spoofax.intellij.IdentifierUtils;

/**
 * Creates the builder menu for a language.
 */
@Singleton
public final class BuilderMenuBuilder {

    @NotNull
    private final IMenuService menuService;

    @Inject
    private BuilderMenuBuilder(@NotNull final IMenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * Builds the builder menu for the specified language implementation.
     *
     * @param implementation The language implementation.
     * @return The built builder menu.
     */
    public IDynamicAction build(@NotNull final ILanguageImpl implementation) {
        Iterable<IMenuItem> items = this.menuService.menuItems(implementation);

        DefaultActionGroup group = new DefaultActionGroup(implementation.belongsTo().name(), true);
        BuilderMenuBuilderState state = new BuilderMenuBuilderState(group);
        for (IMenuItem item : items) {
            item.accept(state);
        }

        return new DynamicActionGroup(group, IdeActions.GROUP_MAIN_MENU);
    }

    private final class BuilderMenuBuilderState implements IMenuItemVisitor {

        @NotNull
        private final DefaultActionGroup group;

        public BuilderMenuBuilderState(@NotNull final DefaultActionGroup group) {
            this.group = group;
        }

        @Override
        public void visitMenu(final IMenu menu) {
            this.group.add(createMenu(menu));
        }

        @Override
        public void visitAction(final IAction action) {
            this.group.add(createAction(action));
        }

        @Override
        public void visitSeparator(final Separator separator) {
            this.group.add(createSeparator(separator));
        }

        @Override
        public void visitMenuItem(final IMenuItem item) {
            throw new MetaborgRuntimeException("Unhandled menu item: " + item.getClass());
        }

        @NotNull
        private AnAction createAction(@NotNull final IAction action) {
            return new AnActionWithId(IdentifierUtils.create("SPOOFAX_MENU_"), action.name()) {
                @Override
                public void actionPerformed(@NotNull final AnActionEvent anActionEvent) {
                    System.out.println("Invoked action!");
                }
            };
        }

        @NotNull
        private com.intellij.openapi.actionSystem.Separator createSeparator(@NotNull final Separator separator) {
            if (StringUtils.isNotEmpty(separator.name())) {
                // NOTE: IntelliJ has support for named separators. If, in the future,
                // the name is not an empty string, it is displayed in the menu as expected.
                return new com.intellij.openapi.actionSystem.Separator(separator.name());
            } else {
                return com.intellij.openapi.actionSystem.Separator.getInstance();
            }
        }

        @NotNull
        private DefaultActionGroup createMenu(@NotNull final IMenu menu) {
            DefaultActionGroup subGroup = new DefaultActionGroup(menu.name(), true);
            BuilderMenuBuilderState state = new BuilderMenuBuilderState(subGroup);
            for (IMenuItem item : menu.items()) {
                item.accept(state);
            }
            return subGroup;
        }
    }

}
