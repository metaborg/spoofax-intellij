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
 * Spoofax state serialization.
 *
 * The configuration is represented by a <code>State</code> object. The configuration is persisted across IntelliJ
 * IDEA sessions by a <code>Service</code>. The configuration is loaded into a JPS session by a <code>Serializer</code>,
 * and represented in JPS by a <code>Config</code> object. The serializer doesn't support saving configuration, but
 * this is not needed from a JPS session.
 *
 * For global (application-wide) configuration, these classes are used:
 * <ul>
 *     <li>{@link org.metaborg.spoofax.intellij.serialization.SpoofaxGlobalState}</li>
 *     <li>{@link org.metaborg.spoofax.intellij.serialization.SpoofaxGlobalConfig}</li>
 *     <li>{@link org.metaborg.spoofax.intellij.serialization.SpoofaxGlobalService}</li>
 *     <li>{@link org.metaborg.spoofax.intellij.serialization.SpoofaxGlobalSerializer}</li>
 * </ul>
 *
 * For project-wide configuration, these classes are used:
 * <ul>
 *     <li>{@link org.metaborg.spoofax.intellij.serialization.SpoofaxProjectState}</li>
 *     <li>{@link org.metaborg.spoofax.intellij.serialization.SpoofaxProjectConfig}</li>
 *     <li>{@link org.metaborg.spoofax.intellij.serialization.SpoofaxProjectService}</li>
 *     <li>{@link org.metaborg.spoofax.intellij.serialization.SpoofaxProjectSerializer}</li>
 * </ul>
 *
 * Currently there is no support for module-specific configuration.
 *
 */
@NonNullByDefault
package org.metaborg.spoofax.intellij.serialization;

import org.metaborg.core.NonNullByDefault;