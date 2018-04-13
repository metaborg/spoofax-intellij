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

import javax.annotation.Nullable;
import javax.swing.*;

/**
 * A language implementation node.
 */
public final class LanguageImplNode extends TreeNodeWithValue<ILanguageImpl, LanguageImplNode>
        implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguageImpl> {

    private final LanguageIdentifier id;

    /**
     * Initializes a new instance of the {@link LanguageImplNode} class.
     *
     * @param languageImpl The language implementation.
     */
    public LanguageImplNode(final ILanguageImpl languageImpl) {
        super(languageImpl);
        this.id = languageImpl.id();
    }

    /**
     * Initializes a new instance of the {@link LanguageImplNode} class.
     *
     * @param id The language identifier.
     */
    public LanguageImplNode(final LanguageIdentifier id) {
        super(null);
        this.id = id;
    }

    /**
     * Gets the language identifier.
     *
     * @return The language identifier.
     */
    public LanguageIdentifier id() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object getValueOfColumn(final ModelColumnInfo<LanguageImplNode> column) {
        return this.id();
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
        return this.id().id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getGroupId() {
        return this.id().groupId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageVersion getVersion() {
        return this.id().version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageStatus getStatus() {
        return null;
    }
}