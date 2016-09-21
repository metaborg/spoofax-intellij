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
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.module.JpsModuleType;
import org.metaborg.intellij.jps.configuration.JpsMetaborgModuleConfig;

/**
 * A JPS Spoofax module type, used to identify Spoofax JPS modules.
 */
@Singleton
public final class JpsMetaborgModuleType implements JpsModuleType<JpsMetaborgModuleConfig> {

    /**
     * Initializes a new instance of the {@link JpsMetaborgModuleType} class.
     */
    @Inject
    public JpsMetaborgModuleType() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public JpsElementChildRole<JpsMetaborgModuleConfig> getPropertiesRole() {
        return JpsMetaborgModuleConfig.ROLE;
    }
}