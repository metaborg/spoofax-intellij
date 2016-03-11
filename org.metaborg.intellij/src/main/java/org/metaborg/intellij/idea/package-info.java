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