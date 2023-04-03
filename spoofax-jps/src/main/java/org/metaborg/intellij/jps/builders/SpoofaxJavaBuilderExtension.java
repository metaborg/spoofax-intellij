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

package org.metaborg.intellij.jps.builders;

import com.google.inject.*;
import org.jetbrains.jps.builders.java.JavaBuilderExtension;
import org.jetbrains.jps.model.module.JpsModuleType;
import org.metaborg.intellij.jps.JpsMetaborgModuleType;
import org.metaborg.intellij.jps.SpoofaxJpsPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The Spoofax Java builder extension.
 */
public final class SpoofaxJavaBuilderExtension extends JavaBuilderExtension {

    private JpsMetaborgModuleType moduleType;

    /**
     * This instance is created by JPS plugin system.
     * Do not call this constructor manually.
     */
    public SpoofaxJavaBuilderExtension() {
        super();
        SpoofaxJpsPlugin.plugin().injectMembers(this);
    }

    @SuppressWarnings("unused")
    @Inject
    private void inject(final JpsMetaborgModuleType moduleType) {
        this.moduleType = moduleType;
    }

    @Override
    public Set<? extends JpsModuleType<?>> getCompilableModuleTypes() {
        return new HashSet<>(Arrays.asList(this.moduleType));
    }
}
