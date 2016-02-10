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
 * The Spoofax for IntelliJ plugin's root package.
 *
 * This package contains the IntelliJ IDEA and JPS-specific packages,
 * and packages with functionality that is shared between IDEA and JPS.
 *
 * An IntelliJ plugin consists of two parts: the IDEA plugin and the JPS plugin.
 * The IDEA plugin is configured through the <code>META-INF/plugin.xml</code>
 * file. This file also describes the JPS plugin and its dependencies, as the
 * JPS plugin is run in a completely separate process from IntelliJ IDEA.
 *
 * To run or debug the IDEA plugin, use the <code>IntelliJ Plugin</code>
 * run/debug configuration or the <code>runIdea</code> Gradle task. You can
 * see whether the plugin was loaded correctly on the Settings/Plugin tab.
 *
 * To debug the JPS plugin, use the <code>IntelliJ Plugin (Debug JPS)</code>
 * run configuration or the <code>debugIdea</code> Gradle task,
 * and then connect a remote debugger (e.g. the <code>JPS Plugin</code> debug
 * configuration) to <code>localhost</code> port 5005.
 *
 */
@NonNullByDefault
package org.metaborg.intellij;