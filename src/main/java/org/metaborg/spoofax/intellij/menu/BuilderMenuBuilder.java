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
import org.metaborg.spoofax.core.menu.TransformAction;
import org.metaborg.spoofax.intellij.IdentifierUtils;
import org.metaborg.spoofax.intellij.factories.IBuilderActionGroupFactory;
import org.metaborg.spoofax.intellij.factories.ITransformIdeaActionFactory;

/**
 * Creates the builder menu for a language.
 */
@Singleton
public final class BuilderMenuBuilder {

    @NotNull
    private final IMenuService menuService;
    @NotNull
    private final IBuilderActionGroupFactory builderActionGroupFactory;
    @NotNull
    private final ITransformIdeaActionFactory transformationActionFactory;

    @Inject
    private BuilderMenuBuilder(@NotNull final IMenuService menuService, @NotNull final IBuilderActionGroupFactory builderActionGroupFactory, @NotNull final ITransformIdeaActionFactory transformationActionFactory) {
        this.menuService = menuService;
        this.builderActionGroupFactory = builderActionGroupFactory;
        this.transformationActionFactory = transformationActionFactory;
    }

    /**
     * Builds the builder menu for the specified language implementation.
     *
     * @param implementation The language implementation.
     * @return The built builder menu.
     */
    public DefaultActionGroup build(@NotNull final ILanguageImpl implementation) {
        Iterable<IMenuItem> items = this.menuService.menuItems(implementation);

        DefaultActionGroup group = builderActionGroupFactory.create(implementation);
        BuilderMenuBuilderState state = new BuilderMenuBuilderState(group, implementation, this.transformationActionFactory);
        for (IMenuItem item : items) {
            item.accept(state);
        }

        return group;
    }

    private static final class BuilderMenuBuilderState implements IMenuItemVisitor {

        @NotNull
        private final DefaultActionGroup group;
        @NotNull
        private final ILanguageImpl implementation;
        @NotNull
        private final ITransformIdeaActionFactory transformationActionFactory;

        public BuilderMenuBuilderState(@NotNull final DefaultActionGroup group, @NotNull final ILanguageImpl implementation, @NotNull final ITransformIdeaActionFactory transformationActionFactory) {
            this.group = group;
            this.implementation = implementation;
            this.transformationActionFactory = transformationActionFactory;
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
            String id = IdentifierUtils.create("SPOOFAX_MENU_");
            if (action instanceof TransformAction) {
                return this.transformationActionFactory.create(id,
                                                               this.implementation,
                                                               (TransformAction)action);
            } else {
                return new AnActionWithId(id, action.name()) {

                    @Override
                    public void actionPerformed(final AnActionEvent anActionEvent) {
                        // TODO: Show an error bubble: this kind of action is not supported.
                    }
                };
            }
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
            BuilderMenuBuilderState state = new BuilderMenuBuilderState(subGroup, this.implementation, this.transformationActionFactory);
            for (IMenuItem item : menu.items()) {
                item.accept(state);
            }
            return subGroup;
        }
    }

}
