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
 *     SpoofaxJpsPlugin.plugin().injectMembers(this);
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