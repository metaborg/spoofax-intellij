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

/**
 * Configuration classes that are shared between IntelliJ IDEA and JPS.
 *
 * There is an application-wide configuration, multiple project configurations, and multiple module
 * configurations.
 *
 * The application-wide configuration is stored by IntelliJ IDEA in the
 * <code>%idea.config.path%/options/metaborg.xml</code> file.
 *
 * The project-specific configuration is stored by IntelliJ IDEA in the
 * <code>$PROJECT_ROOT$/.idea/metaborg.xml</code> file.
 *
 * The module-specific configuration is stored by IntelliJ IDEA in the module's <code>.iml</code> file.
 *
 */
@NonNullByDefault
package org.metaborg.intellij.configuration;

import org.metaborg.intellij.NonNullByDefault;