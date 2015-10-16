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
import java.util.List;

public final class LanguageImplTableModel extends ListTableModel<LanguageImplItem> implements ItemRemovable {

    @NotNull final ModuleConfigurationState state;
    @NotNull final ILanguageService languageService;

    @Inject
    private LanguageImplTableModel(
            @Assisted @NotNull final ModuleConfigurationState state,
            @NotNull final ILanguageService languageService) {
        super(LANGUAGE_COLUMN_INFO, IMPLEMENTATION_COLUMN_INFO);
        this.state = state;
        this.languageService = languageService;

        ArrayList<LanguageImplItem> languages = new ArrayList<>();
        for (ILanguage language : this.languageService.getAllLanguages()) {
            // TODO: Get project active implementation!
            LanguageImplItem item = new LanguageImplItem(language);
            languages.add(item);
        }
        setItems(languages);
    }

    /*
     * The columns.
     */

    private static final String LANGUAGE_COLUMN_NAME = "Language";
    private static final ColumnInfo<LanguageImplItem, ILanguage> LANGUAGE_COLUMN_INFO = new ColumnInfo<LanguageImplItem, ILanguage>(LANGUAGE_COLUMN_NAME) {
        @Nullable
        @Override
        public ILanguage valueOf(final LanguageImplItem item) {
            return item.language();
        }

        @Override
        public boolean isCellEditable(final LanguageImplItem item) {
            return false;
        }
//
//        @Override
//        public Class<?> getColumnClass() {
//            return ILanguage.class;
//        }


        @Nullable
        @Override
        public TableCellRenderer getRenderer(final LanguageImplItem item) {
            return new LanguageItemRenderer();
        }

    };

    private static final String IMPLEMENTATION_COLUMN_NAME = "Implementation";
    private static final ColumnInfo<LanguageImplItem, ILanguageImpl> IMPLEMENTATION_COLUMN_INFO = new ColumnInfo<LanguageImplItem, ILanguageImpl>(IMPLEMENTATION_COLUMN_NAME) {
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
        public TableCellRenderer getCustomizedRenderer(final LanguageImplItem o, final TableCellRenderer renderer) {
            return super.getCustomizedRenderer(o, renderer);
        }

        @Nullable
        @Override
        public TableCellRenderer getRenderer(final LanguageImplItem item) {
            return new ComboBoxTableRenderer<ILanguageImpl>(item.getImplementations().toArray(new ILanguageImpl[0])) {

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
            final ComboBox comboBox = new ComboBox(new CollectionComboBoxModel(item.getImplementations(), item));
            comboBox.setRenderer(new ListCellRendererWrapper<ILanguageImpl>() {

                @Override
                public void customize(final JList list,
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
}
