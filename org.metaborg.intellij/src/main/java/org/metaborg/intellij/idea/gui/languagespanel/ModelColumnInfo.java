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

import com.intellij.util.ui.*;
import org.jetbrains.annotations.*;

/**
 * Column info for nodes that implement the {@link IModelItem} interface.
 */
public class ModelColumnInfo<Item extends IModelItem<Item>> extends ColumnInfo<Item, Object> {

    /**
     * Initializes a new instance of the {@link ModelColumnInfo} class.
     *
     * @param name The name of the column.
     */
    public ModelColumnInfo(final String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object valueOf(final Item item) {
        return item.getValueOfColumn(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(final Item item, @Nullable final Object value) {
        item.setValueOfColumn(this, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(final Item item) {
        return item.canEditValueOfColumn(this);
    }

}
