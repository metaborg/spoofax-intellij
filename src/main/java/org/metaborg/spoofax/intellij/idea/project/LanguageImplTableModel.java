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
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.spoofax.intellij.CollectionUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;

public final class LanguageImplTableModel extends ListTableModel<ILanguageImpl> implements ItemRemovable {

    @NotNull final ModuleConfigurationState state;
    @NotNull final ILanguageService languageService;

    @Inject
    private LanguageImplTableModel(
            @Assisted @NotNull final ModuleConfigurationState state,
            @NotNull final ILanguageService languageService) {
        super(LANGUAGE_COLUMN_INFO, IMPLEMENTATION_COLUMN_INFO);
        this.state = state;
        this.languageService = languageService;

        ArrayList<ILanguageImpl> languages = new ArrayList<>();
        for (ILanguage language : this.languageService.getAllLanguages()) {
            // TODO: Get project active implementation!
            languages.add(language.activeImpl());
        }
        setItems(languages);
    }

    /*
     * The columns.
     */

    private static final String LANGUAGE_COLUMN_NAME = "Language";
    private static final ColumnInfo<ILanguageImpl, ILanguage> LANGUAGE_COLUMN_INFO = new ColumnInfo<ILanguageImpl, ILanguage>(LANGUAGE_COLUMN_NAME) {
        @Nullable
        @Override
        public ILanguage valueOf(final ILanguageImpl item) {
            return item.belongsTo();
        }

        @Override
        public boolean isCellEditable(final ILanguageImpl item) {
            return false;
        }

        @Override
        public Class<?> getColumnClass() {
            return ILanguage.class;
        }
    };

    private static final String IMPLEMENTATION_COLUMN_NAME = "Implementation";
    private static final ColumnInfo<ILanguageImpl, ILanguageImpl> IMPLEMENTATION_COLUMN_INFO = new ColumnInfo<ILanguageImpl, ILanguageImpl>(IMPLEMENTATION_COLUMN_NAME) {
        @Nullable
        @Override
        public ILanguageImpl valueOf(final ILanguageImpl item) {
            return item;
        }

        @Override
        public boolean isCellEditable(final ILanguageImpl item) {
            return false;
        }

        @Override
        public Class<?> getColumnClass() {
            return ILanguageImpl.class;
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(final ILanguageImpl item) {
            return new ComboBoxTableRenderer<ILanguageImpl>(CollectionUtils.toArray(item.belongsTo().impls(),
                                                                                      ILanguageImpl.class)) {
                @Override
                protected String getTextFor(@NotNull final ILanguageImpl value) {
                    return value.id().toString();
                }
            };
        }

        @Nullable
        @Override
        public TableCellEditor getEditor(final ILanguageImpl item) {
            final ComboBox comboBox = new ComboBox(new CollectionComboBoxModel(CollectionUtils.toList(item.belongsTo().impls()), item));
            comboBox.setRenderer(new ListCellRendererWrapper<ILanguageImpl>() {

                @Override
                public void customize(final JList list,
                                      final ILanguageImpl value,
                                      final int index,
                                      final boolean selected,
                                      final boolean hasFocus) {
                    setText(value.id().toString());
                }
            });
            return new DefaultCellEditor(comboBox);
        }
    };
}
