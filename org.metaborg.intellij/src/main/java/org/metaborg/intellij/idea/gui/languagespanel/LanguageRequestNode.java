/*
 * Copyright © 2015-2016
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