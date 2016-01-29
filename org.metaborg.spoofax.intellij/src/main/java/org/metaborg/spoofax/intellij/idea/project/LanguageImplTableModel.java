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
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ComboBoxTableRenderer;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ItemRemovable;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;

public final class LanguageImplTableModel extends ListTableModel<LanguageImplItem> implements ItemRemovable {

    private static final String LANGUAGE_COLUMN_NAME = "Language";
    private static final ColumnInfo<LanguageImplItem, ILanguage> LANGUAGE_COLUMN_INFO =
            new ColumnInfo<LanguageImplItem, ILanguage>(LANGUAGE_COLUMN_NAME) {

                @Nullable
                @Override
                public ILanguage valueOf(final LanguageImplItem item) {
                    return item.language();
                }

                @Override
                public boolean isCellEditable(final LanguageImplItem item) {
                    return false;
                }

                @Nullable
                @Override
                public TableCellRenderer getRenderer(final LanguageImplItem item) {
                    return new LanguageItemRenderer();
                }

            };
    private static final String IMPLEMENTATION_COLUMN_NAME = "Implementation";

    /*
     * The columns.
     */
    private static final ColumnInfo<LanguageImplItem, ILanguageImpl> IMPLEMENTATION_COLUMN_INFO =
            new ColumnInfo<LanguageImplItem, ILanguageImpl>(IMPLEMENTATION_COLUMN_NAME) {
                @Nullable
                @Override
                public ILanguageImpl valueOf(final LanguageImplItem item) {
                    return item.currentImplementation();
                }

                @Override
                public boolean isCellEditable(final LanguageImplItem item) {
                    return true;
                }

                @Override
                public void setValue(final LanguageImplItem item, final ILanguageImpl value) {
                    item.setCurrentImplementation(value);
                }

                @Override
                public TableCellRenderer getCustomizedRenderer(
                        final LanguageImplItem o,
                        final TableCellRenderer renderer) {
                    return super.getCustomizedRenderer(o, renderer);
                }

                @Nullable
                @Override
                public TableCellRenderer getRenderer(final LanguageImplItem item) {
                    final java.util.List<ILanguageImpl> var = item.getImplementations();
                    return new ComboBoxTableRenderer<ILanguageImpl>(var.toArray(new ILanguageImpl[var.size()])) {

                        /**
                         * Gets the text to display for the value.
                         *
                         * Note that this method is not called for <code>null</code> values,
                         * which simply display an empty string.
                         *
                         * @param value The value, which is never <code>null</code>.
                         * @return The text.
                         */
                        @Override
                        protected String getTextFor(@NotNull final ILanguageImpl value) {
                            return value.id().version.toString();
                        }
                    };
                }

                @Nullable
                @Override
                public TableCellEditor getEditor(final LanguageImplItem item) {
                    final ComboBox comboBox = new ComboBox(new CollectionComboBoxModel(
                            item.getImplementations(),
                            item
                    ));
                    comboBox.setRenderer(new ListCellRendererWrapper<ILanguageImpl>() {

                        @Override
                        public void customize(
                                final JList list,
                                final ILanguageImpl value,
                                final int index,
                                final boolean selected,
                                final boolean hasFocus) {
                            if (value != null)
                                setText(value.id().version.toString());
                            else
                                setText("(none)");
                        }
                    });
                    return new DefaultCellEditor(comboBox);
                }
            };
    @NotNull
    final ModuleConfigurationState state;
    @NotNull
    final ILanguageService languageService;

    @Inject
    /* package private */ LanguageImplTableModel(
            @Assisted @NotNull final ModuleConfigurationState state,
            @NotNull final ILanguageService languageService) {
        super(LANGUAGE_COLUMN_INFO, IMPLEMENTATION_COLUMN_INFO);
        this.state = state;
        this.languageService = languageService;

        final ArrayList<LanguageImplItem> languages = new ArrayList<>();
        for (final ILanguage language : this.languageService.getAllLanguages()) {
            // TODO: Get project active implementation!
            final LanguageImplItem item = new LanguageImplItem(language);
            languages.add(item);
        }
        setItems(languages);
    }
}
