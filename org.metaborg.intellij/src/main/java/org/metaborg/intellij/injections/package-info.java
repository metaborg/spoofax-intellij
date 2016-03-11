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