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
 * IntelliJ IDEA extension functionality.
 *
 * Normal plugins register their IDEA extensions in <code>META-INF/plugin.xml</code>
 * under the <code>extensions</code> element, which are then automatically instantiated
 * at plugin startup. However, since we want to be able to load languages dynamically,
 * we have to do this all manually.
 *
 * Every extension point has a unique identifier (e.g.
 * <code>com.intellij.lang.syntaxHighlighterFactory</code>) and an associated class
 * (e.g. {@link com.intellij.openapi.fileTypes.SyntaxHighlighterFactoryEP}).
 * Most extension classes expect a class definition and instantiate that class when
 * needed. We override those extension classes to provide a fixed instance
 * of the class instead. This gives us more control and the opportunity to inject
 * the instance using Guice.
 *
 * Our own extension classes extend the original extension class, and additionally implement
 * the {@link org.metaborg.intellij.idea.extensions.IExtensionPointValue} interface. The functions in
 * {@link org.metaborg.intellij.idea.extensions.ExtensionUtils} are used
 * to register or unregister an extension point.
 *
 * Apart from the IntelliJ IDEA pre-defined extension points, you can define your own. This
 * can be used, for example, to allow other plugins to interface with this plugin.
 */
@NonNullByDefault
package org.metaborg.intellij.idea.extensions;

import org.metaborg.intellij.*;