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

package org.metaborg.intellij.idea.facets;

import com.google.inject.*;
import com.intellij.facet.ui.*;
import com.intellij.ide.util.frameworkSupport.*;
import com.intellij.openapi.roots.*;
import org.metaborg.intellij.idea.*;
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
