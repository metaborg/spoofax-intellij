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

package org.metaborg.intellij.idea.facets;

import com.intellij.facet.*;
import com.intellij.facet.ui.*;
import com.intellij.openapi.util.*;
import org.jdom.*;

/**
 * The configuration of the Metaborg facet.
 */
public class MetaborgFacetConfiguration implements FacetConfiguration {

    /**
     * {@inheritDoc}
     */
    @Override
    public FacetEditorTab[] createEditorTabs(final FacetEditorContext editorContext, final FacetValidatorsManager validatorsManager) {
        return new FacetEditorTab[] {
                new MetaborgFacetEditorTab(editorContext, validatorsManager)
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readExternal(final Element element)
            throws InvalidDataException {
        // Not implemented: deprecated.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeExternal(final Element element)
            throws WriteExternalException {
        // Not implemented: deprecated.
    }
}
