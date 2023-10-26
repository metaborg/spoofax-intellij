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

package org.metaborg.intellij.idea.gui.languagespanel;

import jakarta.annotation.Nullable;

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
     * @param value  The new value.
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
