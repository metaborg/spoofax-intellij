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

package org.metaborg.intellij.jps;

import com.google.inject.*;
import org.jetbrains.annotations.*;
import org.jetbrains.jps.model.serialization.*;
import org.jetbrains.jps.model.serialization.facet.*;
import org.jetbrains.jps.model.serialization.module.*;
import org.metaborg.intellij.jps.configuration.*;

import java.util.*;

/**
 * Returns the serializers used to carry information from IntelliJ to JPS.
 */
@Singleton
public final class JpsSpoofaxModelSerializerExtension extends JpsModelSerializerExtension {

    private MetaborgApplicationConfigDeserializer applicationConfigDeserializer;
    private MetaborgProjectConfigDeserializer projectConfigDeserializer;
    private MetaborgModuleConfigDeserializer moduleConfigDeserializer;
    private MetaborgFacetConfigDeserializer facetConfigDeserializer;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public JpsSpoofaxModelSerializerExtension() {
        SpoofaxJpsPlugin.plugin().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final MetaborgApplicationConfigDeserializer applicationConfigDeserializer,
                        final MetaborgProjectConfigDeserializer projectConfigDeserializer,
                        final MetaborgModuleConfigDeserializer moduleConfigDeserializer,
                        final MetaborgFacetConfigDeserializer facetConfigDeserializer) {
        this.applicationConfigDeserializer = applicationConfigDeserializer;
        this.projectConfigDeserializer = projectConfigDeserializer;
        this.moduleConfigDeserializer = moduleConfigDeserializer;
        this.facetConfigDeserializer = facetConfigDeserializer;
    }

    /**
     * Gets the global extension serializers.
     *
     * @return A list of global extension serializers.
     */
    @Override
    public final List<? extends JpsGlobalExtensionSerializer> getGlobalExtensionSerializers() {
        return Collections.singletonList(this.applicationConfigDeserializer);
    }

    /**
     * Gets the project extension serializers.
     *
     * @return A list of project extension serializers.
     */
    @Override
    public final List<? extends JpsProjectExtensionSerializer> getProjectExtensionSerializers() {
        return Collections.singletonList(this.projectConfigDeserializer);
    }

    /**
     * Gets the module properties serializers.
     *
     * @return A list of module properties serializers.
     */
    @Override
    public final List<? extends JpsModulePropertiesSerializer<?>> getModulePropertiesSerializers() {
        return Collections.singletonList(this.moduleConfigDeserializer);
    }

    /**
     * Gets the facet configuration serializers.
     *
     * @return A list of facet configuration serializers.
     */
    @NotNull
    @Override
    public List<? extends JpsFacetConfigurationSerializer<?>> getFacetConfigurationSerializers() {
        return Collections.singletonList(this.facetConfigDeserializer);
    }
}