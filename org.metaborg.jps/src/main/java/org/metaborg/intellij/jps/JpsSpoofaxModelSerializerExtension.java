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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;
import org.jetbrains.jps.model.serialization.facet.JpsFacetConfigurationSerializer;
import org.jetbrains.jps.model.serialization.module.JpsModulePropertiesSerializer;
import org.metaborg.intellij.jps.configuration.MetaborgApplicationConfigDeserializer;
import org.metaborg.intellij.jps.configuration.MetaborgFacetConfigDeserializer;
import org.metaborg.intellij.jps.configuration.MetaborgModuleConfigDeserializer;
import org.metaborg.intellij.jps.configuration.MetaborgProjectConfigDeserializer;

import java.util.Collections;
import java.util.List;

/**
 * Returns the serializers used to carry information from IntelliJ to JPS.
 */
@Singleton
public final class JpsSpoofaxModelSerializerExtension extends JpsModelSerializerExtension {

    private JpsGlobalExtensionSerializer applicationConfigDeserializer;
    private JpsProjectExtensionSerializer projectConfigDeserializer;
    private JpsModulePropertiesSerializer<?> moduleConfigDeserializer;
    private JpsFacetConfigurationSerializer<?> facetConfigDeserializer;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public JpsSpoofaxModelSerializerExtension() {
        SpoofaxJpsPlugin plugin = SpoofaxJpsPlugin.plugin();
        // This class is loaded by the JPS ServiceLoader, which uses its own class loader
        // instead of the one we want to use. In turn, this causes the @Inject annotation
        // of the inject() method below to be created by the URLClassLoader instead of our
        // own PriorityURLClassLoader. This then prevents the dependency injector from
        // discovering the inject() method below, as the @Inject annotation of the URLClassLoader
        // is different from the PriorityURLClassLoader's @Inject annotation that the
        // dependency injector expects. And this then prevents the object from being injected
        // properly when using `plugin.injectMembers(this)`. Therefore we do it manually.

        // Note that for this trick to work, we can never refer to the class we want to load
        // by its actual name, which is why for the fields and in the inject() method
        // we use a supertype of the classes instead.
        Object instance = plugin.getInstance(MetaborgApplicationConfigDeserializer.class);

        inject(
            plugin.getInstance(MetaborgApplicationConfigDeserializer.class),
            plugin.getInstance(MetaborgProjectConfigDeserializer.class),
            plugin.getInstance(MetaborgModuleConfigDeserializer.class),
            plugin.getInstance(MetaborgFacetConfigDeserializer.class)
        );
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final JpsGlobalExtensionSerializer applicationConfigDeserializer,
                        final JpsProjectExtensionSerializer projectConfigDeserializer,
                        final JpsModulePropertiesSerializer<?> moduleConfigDeserializer,
                        final JpsFacetConfigurationSerializer<?> facetConfigDeserializer) {
        assert applicationConfigDeserializer != null;
        assert projectConfigDeserializer != null;
        assert moduleConfigDeserializer != null;
        assert facetConfigDeserializer != null;

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