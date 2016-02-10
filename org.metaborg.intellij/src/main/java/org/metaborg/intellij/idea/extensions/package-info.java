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
 * the {@link org.metaborg.intellij.idea.extensions.IExtensionPointValue} interface. The functions in {@link ExtensionUtils} are used
 * to register or unregister an extension point.
 *
 * Apart from the IntelliJ IDEA pre-defined extension points, you can define your own. This
 * can be used, for example, to allow other plugins to interface with this plugin.
 */
@NonNullByDefault
package org.metaborg.intellij.idea.extensions;

import org.metaborg.intellij.*;