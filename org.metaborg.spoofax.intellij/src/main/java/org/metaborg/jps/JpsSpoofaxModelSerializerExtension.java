/*
 * Copyright © 2015-2015
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

package org.metaborg.jps;

import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;
import org.jetbrains.jps.model.serialization.module.JpsModulePropertiesSerializer;
import org.metaborg.spoofax.intellij.serialization.SpoofaxGlobalSerializer;
import org.metaborg.spoofax.intellij.serialization.SpoofaxModuleSerializer;
import org.metaborg.spoofax.intellij.serialization.SpoofaxProjectSerializer;

import java.util.Collections;
import java.util.List;

/**
 * Returns the serializers used to carry information from IntelliJ to JPS.
 */
@Singleton
public final class JpsSpoofaxModelSerializerExtension extends JpsModelSerializerExtension {

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public JpsSpoofaxModelSerializerExtension() {
    }

    /**
     * Gets the project extension serializers.
     *
     * @return A list of project extension serializers.
     */
    @NotNull
    @Override
    public final List<? extends JpsProjectExtensionSerializer> getProjectExtensionSerializers() {
        return Collections.singletonList(new SpoofaxProjectSerializer());
    }

    /**
     * Gets the global extension serializers.
     *
     * @return A list of global extension serializers.
     */
    @NotNull
    @Override
    public final List<? extends JpsGlobalExtensionSerializer> getGlobalExtensionSerializers() {
        return Collections.singletonList(new SpoofaxGlobalSerializer());
    }

    /**
     * Gets the module properties serializers.
     *
     * @return A list of module properties serializers.
     */
    @NotNull
    @Override
    public final List<? extends JpsModulePropertiesSerializer<?>> getModulePropertiesSerializers() {
        return Collections.singletonList(new SpoofaxModuleSerializer());
    }
}