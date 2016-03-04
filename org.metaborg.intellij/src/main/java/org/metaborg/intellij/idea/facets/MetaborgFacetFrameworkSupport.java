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

import com.google.inject.*;
import com.intellij.facet.*;
import com.intellij.facet.frameworks.*;
import com.intellij.facet.ui.*;
import com.intellij.facet.ui.libraries.*;
import com.intellij.ide.util.frameworkSupport.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.openapi.roots.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.idea.sdks.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

/**
 * Framework support for the Metaborg Facet.
 *
 * This class is only called for newly created modules/projects.
 * When adding a facet to an existing project, see the {@link MetaborgFacetEditorTab} class.
 */
public class MetaborgFacetFrameworkSupport extends FacetBasedFrameworkSupportProvider<MetaborgFacet> {

    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgFacetFrameworkSupport() {
        super(SpoofaxIdeaPlugin.injector().getInstance(MetaborgFacetType.class));

        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject() {
    }

    @Override
    protected void setupConfiguration(final MetaborgFacet facet,
                                      final ModifiableRootModel rootModel,
                                      final FrameworkVersion version) {
        this.logger.debug("Setting up facet configuration.");

        // Nothing to do.

        this.logger.info("Set up facet configuration.");
    }

    @Override
    protected void onFacetCreated(final MetaborgFacet facet,
                                  final ModifiableRootModel rootModel,
                                  final FrameworkVersion version) {

        this.logger.debug("Applying facet.");

        facet.applyFacet(rootModel);

        this.logger.info("Applied facet.");
    }

}
