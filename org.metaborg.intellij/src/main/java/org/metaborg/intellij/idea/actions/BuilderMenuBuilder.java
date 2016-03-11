/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.actions;

import com.google.inject.*;
import com.intellij.openapi.actionSystem.*;
import org.apache.commons.lang3.*;
import org.metaborg.core.*;
import org.metaborg.core.language.*;
import org.metaborg.core.menu.*;
import org.metaborg.core.menu.Separator;
import org.metaborg.intellij.*;

/**
 * Creates the builder menu for a language.
 */
@Singleton
public final class BuilderMenuBuilder {

    private final IMenuService menuService;
    private final IBuilderActionGroupFactory builderActionGroupFactory;
    private final ITransformIdeaActionFactory transformationActionFactory;

    @Inject
    /* package private */ BuilderMenuBuilder(
            final IMenuService menuService,
            final IBuilderActionGroupFactory builderActionGroupFactory,
            final ITransformIdeaActionFactory transformationActionFactory) {
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
    public DefaultActionGroup build(final ILanguageImpl implementation) {
        final Iterable<IMenuItem> items = this.menuService.menuItems(implementation);

        final DefaultActionGroup group = this.builderActionGroupFactory.create(implementation);
        final BuilderMenuBuilderState state = new BuilderMenuBuilderState(
                group,
                implementation,
                this.transformationActionFactory
        );
        for (final IMenuItem item : items) {
            item.accept(state);
        }

        return group;
    }

    private static final class BuilderMenuBuilderState implements IMenuItemVisitor {

        private final DefaultActionGroup group;
        private final ILanguageImpl implementation;
        private final ITransformIdeaActionFactory transformationActionFactory;

        public BuilderMenuBuilderState(
                final DefaultActionGroup group,
                final ILanguageImpl implementation,
                final ITransformIdeaActionFactory transformationActionFactory) {
            this.group = group;
            this.implementation = implementation;
            this.transformationActionFactory = transformationActionFactory;
        }

        @Override
        public void visitMenu(final IMenu menu) {
            this.group.add(createMenu(menu));
        }

        @Override
        public void visitAction(final IMenuAction action) {
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

        private com.intellij.openapi.actionSystem.Separator createSeparator(final Separator separator) {
            if (StringUtils.isNotEmpty(separator.name())) {
                // NOTE: IntelliJ has support for named separators. If, in the future,
                // the name is not an empty string, it is displayed in the menu as expected.
                return new com.intellij.openapi.actionSystem.Separator(separator.name());
            } else {
                return com.intellij.openapi.actionSystem.Separator.getInstance();
            }
        }

        private AnAction createAction(final IMenuAction action) {
            final String id = IdentifierUtils.create("METABORG_MENU_");
            return this.transformationActionFactory.create(
                    id,
                    action.action(),
                    this.implementation
            );
        }

        private DefaultActionGroup createMenu(final IMenu menu) {
            final DefaultActionGroup subGroup = new DefaultActionGroup(menu.name(), true);
            final BuilderMenuBuilderState state = new BuilderMenuBuilderState(
                    subGroup,
                    this.implementation,
                    this.transformationActionFactory
            );
            for (final IMenuItem item : menu.items()) {
                item.accept(state);
            }
            return subGroup;
        }
    }

}
