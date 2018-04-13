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

package org.metaborg.intellij.idea.graphics;

import com.google.inject.Singleton;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;
import org.metaborg.core.language.*;

import javax.annotation.Nullable;
import javax.swing.*;

/**
 * Default implementation of the {@link IIconManager} interface.
 */
@Singleton
public final class DefaultIconManager implements IIconManager {

    private final Icon defaultIcon;
    private final Icon languageArtifactIcon;

    /**
     * Initializes a new instance of the {@link DefaultIconManager} class.
     */
    public DefaultIconManager() {
        this.defaultIcon = IconLoader.getIcon("/SpoofaxDefault.png");
        this.languageArtifactIcon = AllIcons.FileTypes.Archive;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getDefaultIcon() {
        return this.defaultIcon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getLanguageFileIcon(@Nullable final ILanguage language) {
        // TODO: Get icon from ILanguage facet, otherwise use default.
        return this.getDefaultIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getLanguageArtifactIcon() {
        return this.languageArtifactIcon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getFacetIcon() {
        return this.getDefaultIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getSdkIcon() {
        return this.getDefaultIcon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Icon getSdkIconForAddAction() {
        return this.getDefaultIcon();
    }
}
