/*
 * Copyright © 2015-2016
 *
 * This file is part of org.metaborg.intellij.
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

package org.metaborg.intellij.idea.extensions;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Module;
import org.metaborg.core.*;
import org.metaborg.core.plugin.*;

import java.util.Collection;

public abstract class IntelliJPluginLoader<T extends IServiceModulePlugin> implements IModulePluginLoader {

    protected abstract Collection<T> getPlugins();

    @Override
    public Collection<Module> modules() throws MetaborgException {
        try {
            final Iterable<T> plugins = getPlugins();
            final Collection<Module> modules = Lists.newLinkedList();
            for(final T plugin : plugins)
            {
                Iterables.addAll(modules, plugin.modules());
            }
            return modules;
        } catch(final Exception e) {
            throw new MetaborgException("Unhandled exception while loading module plugins with IntelliJ's IntelliJPluginLoader.", e);
        }
    }
}

