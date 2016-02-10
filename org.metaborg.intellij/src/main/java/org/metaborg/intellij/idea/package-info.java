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
 * Package with functionality specific to the IntelliJ IDEA environment.
 *
 * IntelliJ IDEA is the interactive development environment itself.
 * It uses a custom extension mechanism to load services when needed. The
 * extensions are specified in <code>META-INF/plugin.xml</code> under the
 * <code>extensions</code> element.
 *
 * Instances created by IntelliJ IDEA don't have any dependencies injected into them.
 * To inject dependencies, create an <code>inject</code> method with the
 * dependencies and call the injector from the constructor. This will also inject
 * the logger (if any). Obviously, this forces the dependency fields to be non-final.
 * For example:
 *
 * <pre>
 * public MyService() {
 *     super();
 *     SpoofaxIdeaPlugin.injector().injectMembers(this);
 * }
 *
 * &#064;SuppressWarnings("unused")
 * &#064;Inject
 * private void inject(
 *         final IDependencyService dependencyService,
 *         final IIdentifierService identifierService) {
 *     this.dependencyService = dependencyService;
 *     this.identifierService = identifierService;
 * }
 * </pre>
 */
@NonNullByDefault
package org.metaborg.intellij.idea;

import org.metaborg.intellij.NonNullByDefault;