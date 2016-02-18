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

package org.metaborg.intellij.injections;

import com.google.inject.*;
import com.intellij.openapi.module.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

/**
 * Provides a {@link ModuleType} instance.
 *
 * Usage:
 * <pre>
 * bind(MyModuleType.class).toProvider(new IntelliJModuleTypeProvider<>(MyModuleType.ID));
 * </pre>
 */
public final class IntelliJModuleTypeProvider<T extends ModuleType> implements Provider<T> {

    @InjectLogger
    private ILogger logger;
    private final String moduleID;

    /**
     * Initializes a new instance of the {@link IntelliJModuleTypeProvider} class.
     *
     * @param moduleID The module ID.
     */
    public IntelliJModuleTypeProvider(
            final String moduleID) {
        this.moduleID = moduleID;
    }

    @Override
    public T get() {
        return (T)ModuleTypeManager.getInstance().findByID(this.moduleID);
    }
}
