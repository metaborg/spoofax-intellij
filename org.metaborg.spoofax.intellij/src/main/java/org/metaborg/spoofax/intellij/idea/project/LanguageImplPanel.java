/*
 * Copyright © 2015-2015
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.spoofax.intellij.idea.project;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.ui.TableViewSpeedSearch;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.TableView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.spoofax.intellij.factories.ILanguageImplTableModelFactory;

import javax.swing.*;
import java.awt.*;

public final class LanguageImplPanel extends JPanel {
    @NotNull
    private final ModuleConfigurationState state;
    @NotNull
    private final LanguageImplTableModel model;
    @NotNull
    private final TableView<LanguageImplItem> table;
    @NotNull
    private final ILanguageImplTableModelFactory languageImplTableModelFactory;

    @Inject
    /* package private */ LanguageImplPanel(
            @Assisted @NotNull final ModuleConfigurationState state,
            @NotNull final ILanguageImplTableModelFactory languageImplTableModelFactory) {
        super(new BorderLayout());

        this.state = state;
        this.languageImplTableModelFactory = languageImplTableModelFactory;

        this.model = this.languageImplTableModelFactory.create(this.state);
        this.table = new TableView<>(this.model);
        new TableViewSpeedSearch<LanguageImplItem>(this.table) {
            @Nullable
            @Override
            protected String getItemText(@NotNull final LanguageImplItem item) {
                return item.language().name();
            }
        };

        add(createTableWithButtons(), BorderLayout.CENTER);
    }

    private JComponent createTableWithButtons() {
        final ToolbarDecorator decorator = ToolbarDecorator.createDecorator(this.table);
        return decorator.createPanel();
    }
}
