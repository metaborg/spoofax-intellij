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

package org.metaborg.spoofax.intellij.serialization;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsElementFactory;
import org.jetbrains.jps.model.serialization.module.JpsModulePropertiesSerializer;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;

import javax.annotation.Nullable;

public final class SpoofaxModuleSerializer extends JpsModulePropertiesSerializer<JpsDummyElement> {

    public SpoofaxModuleSerializer() {
        super(JpsSpoofaxModuleType.INSTANCE, "SPOOFAX_MODULE", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public final JpsDummyElement loadProperties(@Nullable final Element element) {
        JpsDummyElement result = JpsElementFactory.getInstance().createDummyElement();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveProperties(@NotNull final JpsDummyElement jpsDummyElement, @NotNull final Element element) {
        throw new UnsupportedOperationException("The `saveProperties()` method is not supported.");
    }

}
