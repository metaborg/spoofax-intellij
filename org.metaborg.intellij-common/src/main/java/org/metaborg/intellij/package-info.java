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