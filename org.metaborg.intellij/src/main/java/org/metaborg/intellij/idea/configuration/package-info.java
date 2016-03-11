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
 * Package with IntelliJ IDEA persisted configurations.
 *
 * IntelliJ IDEA will load and store configurations automatically. A persisted
 * configuration implements the {@link com.intellij.openapi.components.PersistentStateComponent}
 * interface and has a 'state' class with the actual data to persist. Additionally, the
 * manager must be annotated with the {@link com.intellij.openapi.components.State} annotation, signifying
 * where the configuration must be stored.
 *
 * The state class (e.g. {@link org.metaborg.intellij.configuration.MetaborgApplicationConfigState})
 * contains the numbers, booleans, strings, collections, maps and enums to serialize. To exclude a field
 * from serialization, annotate the field or getter with the {@link com.intellij.util.xmlb.annotations.Transient}
 * annotation. The state class' default constructor must initialize all fields to their default values,
 * and it must implement {@link java.lang.Object#equals}.
 *
 * The state is automatically loaded and persisted when the class is registered in the
 * <code>META-INF/plugin.xml</code> file and requested through
 * {@link com.intellij.openapi.components.ServiceManager#getService}.
 *
 * For new projects the project state is automatically persisted when it is set in the
 * {@link com.intellij.ide.util.projectWizard.ModuleBuilder#setupRootModel} method.
 */
@NonNullByDefault
package org.metaborg.intellij.idea.configuration;

import org.metaborg.intellij.*;