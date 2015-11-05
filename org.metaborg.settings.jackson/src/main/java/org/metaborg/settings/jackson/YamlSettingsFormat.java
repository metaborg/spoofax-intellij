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

package org.metaborg.settings.jackson;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Preconditions;
import org.metaborg.settings.ISettingsFactory;

/**
 * Settings serializer/deserializer using YAML.
 * <p>
 * Types can use custom (de)serializers through an annotation. For example:
 * <pre>
 *     @JsonSerialize(using = VersionSerializer.class)
 *     @JsonDeserialize(using = VersionDeserializer.class)
 *     public class Version {
 *         // ...
 *     }
 * </pre>
 */
public final class YamlSettingsFormat extends JacksonSettingsFormat {

    public YamlSettingsFormat(final ISettingsFactory settingsFactory) {
        super(settingsFactory, new YAMLFactory());

        Preconditions.checkNotNull(settingsFactory);
    }

}
