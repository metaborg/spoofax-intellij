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

package org.metaborg.intellij.jps.serialization;

import org.jdom.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.serialization.module.*;
import org.metaborg.intellij.jps.*;

import javax.annotation.*;

/**
 * Deserializes module-specific configuration in JPS.
 */
public final class SpoofaxModuleSerializer extends JpsModulePropertiesSerializer<JpsDummyElement> {

    public static final String NAME = "SpoofaxModuleService";

    public SpoofaxModuleSerializer() {
        super(JpsMetaborgModuleType.INSTANCE, "METABORG_MODULE", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JpsDummyElement loadProperties(@Nullable final Element element) {
        return JpsElementFactory.getInstance().createDummyElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveProperties(final JpsDummyElement jpsDummyElement, final Element element) {
        throw new UnsupportedOperationException("The `saveProperties()` method is not supported.");
    }

}
