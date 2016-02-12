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