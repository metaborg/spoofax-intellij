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

import javax.annotation.Nullable;
import javax.swing.Icon;

import org.metaborg.core.config.ILanguageComponentConfig;
import org.metaborg.core.language.ILanguageDiscoveryRequest;
import org.metaborg.core.language.LanguageVersion;

/**
 * A language request node.
 */
public final class LanguageRequestNode extends TreeNodeWithValue<ILanguageDiscoveryRequest, LanguageRequestNode>
        implements ITreeNodeWithIcon, ILanguageTreeNode<ILanguageDiscoveryRequest>  {

    /**
     * Initializes a new instance of the {@link LanguageRequestNode} class.
     *
     * @param request The language request.
     */
    public LanguageRequestNode(final ILanguageDiscoveryRequest request) {
        super(request);
    }

    /**
     * Gets the language request.
     *
     * @return The language request.
     */
    public ILanguageDiscoveryRequest getRequest() {
        @Nullable final ILanguageDiscoveryRequest request = this.getValue();
        assert request != null;
        return request;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public Object getValueOfColumn(final ModelColumnInfo<LanguageRequestNode> column) {
        final ILanguageDiscoveryRequest request = this.getRequest();
        if (request.available()) {
            @Nullable final ILanguageComponentConfig config = request.config();
            assert config != null : "The configuration should not be null since the request is available.";
            return config.identifier();
        } else {
            return request.location();
        }
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
        final ILanguageDiscoveryRequest request = this.getRequest();
        if (request.available()) {
            @Nullable final ILanguageComponentConfig config = request.config();
            assert config != null : "The configuration should not be null since the request is available.";
            return config.identifier().id;
        } else {
            return request.location().toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public String getGroupId() {
        final ILanguageDiscoveryRequest request = this.getRequest();
        if (request.available()) {
            @Nullable final ILanguageComponentConfig config = request.config();
            assert config != null : "The configuration should not be null since the request is available.";
            return config.identifier().groupId;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageVersion getVersion() {
        final ILanguageDiscoveryRequest request = this.getRequest();
        if (request.available()) {
            @Nullable final ILanguageComponentConfig config = request.config();
            assert config != null : "The configuration should not be null since the request is available.";
            return config.identifier().version;
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public LanguageStatus getStatus() {
        final ILanguageDiscoveryRequest request = this.getRequest();
        if (request.available()) {
            return LanguageStatus.Standby;
        } else {
            return LanguageStatus.Error;
        }
    }
}