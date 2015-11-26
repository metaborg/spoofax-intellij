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

package org.metaborg.idea.gui;

import javax.annotation.Nullable;

/**
 * An item in a model.
 */
public interface IModelItem<Item extends IModelItem<Item>> {

    /**
     * Gets the value of this item in the specified column.
     *
     * @param column The column.
     * @return The current value.
     */
    @Nullable
    Object getValueOfColumn(ModelColumnInfo<Item> column);

    /**
     * Sets the value of this item in the specified column.
     *
     * @param column The column.
     * @param value The new value.
     */
    void setValueOfColumn(ModelColumnInfo<Item> column, @Nullable Object value);

    /**
     * Gets whether the value of this item in the specified column can be edited.
     *
     * @param column The column.
     * @return <code>true</code> when the value can be edited;
     * otherwise, <code>false</code>.
     */
    boolean canEditValueOfColumn(ModelColumnInfo<Item> column);
}
