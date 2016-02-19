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

package org.metaborg.intellij.jps.builders;

import com.google.common.collect.*;
import com.google.inject.*;
import org.jetbrains.jps.builders.java.*;
import org.jetbrains.jps.model.module.*;
import org.metaborg.intellij.jps.*;

import java.util.*;

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
        SpoofaxJpsPlugin.injector().injectMembers(this);
    }

    @SuppressWarnings("unused")
    @Inject
    private void inject(final JpsMetaborgModuleType moduleType) {
        this.moduleType = moduleType;
    }

    @Override
    public Set<? extends JpsModuleType<?>> getCompilableModuleTypes() {
        return Sets.newHashSet(this.moduleType);
    }
}
