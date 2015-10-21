package org.metaborg.spoofax.intellij.idea.project;

import com.intellij.openapi.roots.ui.CellAppearanceEx;
import com.intellij.openapi.roots.ui.util.SimpleTextCellAppearance;
import com.intellij.ui.ColoredTableCellRenderer;
import org.metaborg.core.language.ILanguage;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxIcons;

import javax.swing.*;

public final class LanguageItemRenderer extends ColoredTableCellRenderer {
    @Override
    protected void customizeCellRenderer(final JTable table,
                                         final Object value,
                                         final boolean selected,
                                         final boolean hasFocus,
                                         final int row,
                                         final int column) {
        setPaintFocusBorder(false);
        setFocusBorderAroundIcon(true);
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        if (value instanceof ILanguage) {
            final ILanguage item = (ILanguage)value;
            getCellAppearance(item).customize(this);
            setToolTipText("Spoofax tooltip!");
        }
    }

    private CellAppearanceEx getCellAppearance(ILanguage item) {
        final Icon icon = SpoofaxIcons.INSTANCE.Default;
        final String name = item.name();
        return SimpleTextCellAppearance.regular(name, icon);
    }
}
