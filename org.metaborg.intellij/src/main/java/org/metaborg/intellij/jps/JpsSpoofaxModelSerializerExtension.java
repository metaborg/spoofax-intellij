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

package org.metaborg.intellij.jps;

import com.google.inject.*;
import org.jetbrains.jps.model.serialization.*;
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

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public JpsSpoofaxModelSerializerExtension() {
        SpoofaxJpsPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(final MetaborgApplicationConfigDeserializer applicationConfigDeserializer,
                        final MetaborgProjectConfigDeserializer projectConfigDeserializer,
                        final MetaborgModuleConfigDeserializer moduleConfigDeserializer) {
        this.applicationConfigDeserializer = applicationConfigDeserializer;
        this.projectConfigDeserializer = projectConfigDeserializer;
        this.moduleConfigDeserializer = moduleConfigDeserializer;
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
}