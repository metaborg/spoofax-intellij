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
import com.intellij.openapi.module.*;
import org.jetbrains.annotations.*;

/**
 * A Metaborg facet.
 *
 * This facet on a module indicates that the module uses files written in a Metaborg language.
 */
public class MetaborgFacet extends Facet {

    public static final FacetTypeId<MetaborgFacet> ID = new FacetTypeId<>(MetaborgFacetType.ID);

    /**
     * Initializes a new instance of the {@link MetaborgFacet} class.
     *
     * @param facetType The facet type.
     * @param module The module to which the facet is applied.
     * @param name The name of the facet.
     * @param configuration The configuration of the facet.
     * @param underlyingFacet The underlying facet.
     */
    public MetaborgFacet(final MetaborgFacetType facetType,
                         final Module module,
                         final String name,
                         final FacetConfiguration configuration,
                         @Nullable final Facet underlyingFacet) {
        super(facetType, module, name, configuration, underlyingFacet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initFacet() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disposeFacet() {

    }


}
