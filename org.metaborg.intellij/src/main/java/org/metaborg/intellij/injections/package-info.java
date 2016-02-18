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
 * Guice custom injectors.
 *
 * For most classes the normal Guice injectors are sufficient. However, to inject an IntelliJ service (those
 * registered as &lt;applicationService&gt; in <code>META-INF/plugin.xml</code>) use the
 * {@link org.metaborg.intellij.injections.IntelliJServiceProviderFactory}. To inject an IntelliJ component (those
 * registered in the &lt;application-components&gt; element of <code>META-INF/plugin.xml</code>) use the
 * {@link org.metaborg.intellij.injections.IntelliJComponentProviderFactory}. To inject another IntelliJ extension
 * (those registered in the &lt;extensions&gt; element of <code>META-INF/plugin.xml</code>),
 * use the {@link org.metaborg.intellij.injections.IntelliJExtensionProviderFactory}. To inject a Java service
 * (those registered in files under the <code>META-INF/services/</code> folder) use the
 * {@link org.metaborg.intellij.injections.JavaServiceProviderFactory}.
 */
@NonNullByDefault
package org.metaborg.intellij.injections;

import org.metaborg.intellij.*;