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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointName;
import org.metaborg.core.*;
import org.metaborg.core.plugin.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public final class IntelliJSpoofaxPluginLoader extends IntelliJPluginLoader<IServiceModulePlugin> {

    private static final Logger logger = Logger.getInstance(IntelliJSpoofaxPluginLoader.class);
    private static final ExtensionPointName<IServiceModulePlugin> EP_NAME
            = ExtensionPointName.create("org.metaborg.intellij.spoofaxPlugin");

    @Override
    protected Collection<IServiceModulePlugin> getPlugins() {
        try {
            return Arrays.asList(EP_NAME.getExtensions());
        } catch (IllegalArgumentException ex) {
            logger.warn("Getting plugins failed: " + ex.getMessage());
            return Collections.emptyList();
        }
    }

}

