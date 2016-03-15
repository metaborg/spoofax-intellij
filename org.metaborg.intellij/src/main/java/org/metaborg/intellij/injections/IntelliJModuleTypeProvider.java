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
