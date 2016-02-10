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
 * Package with functionality specific to the JPS environment.
 *
 * JetBrains Project System (JPS) is the environment in which modules are built.
 * It uses Java's {@link java.util.ServiceLoader} to load services when needed.
 * To bind an implementation <code>MyServiceImpl</code> to a class or interface
 * <code>ISomeService</code>, add a file with the fully qualified name of
 * <code>ISomeService</code> to the <code>META-INF/services/</code> folder of the
 * project. In the file you specify the fully qualified name of <code>MyServiceImpl</code>.
 *
 * Instances created by JPS don't have any dependencies injected into them.
 * To inject dependencies, create an <code>inject</code> method with the
 * dependencies and call the injector from the constructor. This will also inject
 * the logger (if any). Obviously, this forces the dependency fields to be non-final.
 * For example:
 *
 * <pre>
 * public MyService() {
 *     super();
 *     SpoofaxJpsPlugin.injector().injectMembers(this);
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
 *
 * The dependencies for the JPS plugin must be specified in the
 * <code>META-INF/plugin.xml</code> file under the <code>compileServer.plugin</code>
 * element. If this list is incomplete, you'll get {@link java.lang.ClassNotFoundException}
 * or {@link java.lang.NoClassDefFoundError} exceptions. To get a list of dependencies
 * you can paste there, invoke the <code>printJpsDependencies</code> Gradle task.
 *
 */
@NonNullByDefault
package org.metaborg.intellij.jps;

import org.metaborg.intellij.NonNullByDefault;