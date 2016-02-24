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
 * Metaborg project facet.
 *
 * A module with the {@link org.metaborg.intellij.idea.facets.MetaborgFacet}
 * facet is treated as a project that uses a Spoofax language, but does not
 * specify one. To specify a Spoofax language, use a Spoofax Language
 * Specification project, which is a separate module type.
 */
@NonNullByDefault
package org.metaborg.intellij.idea.facets;

import org.metaborg.intellij.*;