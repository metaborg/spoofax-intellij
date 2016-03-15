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

import org.metaborg.core.language.*;

import javax.annotation.*;
import javax.swing.*;

/**
 * A language component node.
 */
public final class LanguageComponentNode extends TreeNodeWithValue<ILanguageComponent, LanguageComponentNode>
        implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguageComponent>  {

    /**
     * Initializes a new instance of this node.
     *
     * @param languageComponent The language component.
     */
    public LanguageComponentNode(final ILanguageComponent languageComponent) {
        super(languageComponent);
    }

    /**
     * Gets the language component.
     *
     * @return The language component.
     */
    public ILanguageComponent getComponent() {
        @Nullable final ILanguageComponent component = this.getValue();
        assert component != null;
        return component;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object getValueOfColumn(final ModelColumnInfo<LanguageComponentNode> column) {
        return this.getComponent().id();
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public String getName() {
        return this.getComponent().id().id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getGroupId() {
        return this.getComponent().id().groupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageVersion getVersion() {
        return this.getComponent().id().version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageStatus getStatus() {
        return LanguageStatus.Loaded;
    }
}