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
 * Metaborg project facet.
 *
 * A module with the {@link org.metaborg.intellij.idea.facets.MetaborgFacet}
 * facet is treated as a project that uses a Spoofax language, but does not
 * specify one. To specify a Spoofax language, use a Spoofax Language
 * Specification project, which is a separate module type.
 */
@NonNullByDefault
package org.metaborg.intellij.idea.facets;

import org.metaborg.intellij.*;