package org.metaborg.spoofax.intellij.idea.project;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.factories.ILanguageImplTableModelFactory;

import javax.swing.*;
import java.awt.*;

public final class LanguageImplPanel extends JPanel {
    @NotNull private final ModuleConfigurationState state;
    @NotNull private final LanguageImplTableModel model;
    @NotNull private final JBTable table;
    @NotNull private final ILanguageImplTableModelFactory languageImplTableModelFactory;

    @Inject
    private LanguageImplPanel(
            @Assisted @NotNull final ModuleConfigurationState state,
            @NotNull final ILanguageImplTableModelFactory languageImplTableModelFactory) {
        super(new BorderLayout());

        this.state = state;
        this.languageImplTableModelFactory = languageImplTableModelFactory;

        this.model = this.languageImplTableModelFactory.create(this.state);
        this.table = new JBTable(this.model);
        this.table.setShowGrid(false);
        this.table.setDragEnabled(false);
        this.table.setIntercellSpacing(new Dimension(0, 0));
        this.table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.table.setDefaultRenderer(ILanguage.class, new LanguageItemRenderer());
        //JComboBox implEditor = new ComboBox(new CollectionListModel<>(ILanguageImpl.class));
        //this.table.setDefaultEditor(ILanguageImpl.class, new DefaultCellEditor(implEditor));

        add(createTableWithButtons(), BorderLayout.CENTER);
    }

    private JComponent createTableWithButtons() {
        final ToolbarDecorator decorator = ToolbarDecorator.createDecorator(this.table);
        final JPanel panel = decorator.createPanel();
        return panel;
    }
}
