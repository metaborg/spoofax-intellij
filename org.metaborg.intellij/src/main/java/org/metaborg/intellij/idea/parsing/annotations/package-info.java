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
 * Source annotations.
 *
 * IntelliJ IDEA invokes the {@link com.intellij.lang.annotation.ExternalAnnotator}
 * implementation (which by definition uses a slow external tool for annotation) to
 * gather information about the source code annotations (e.g. errors and warnings).
 *
 * First {@link com.intellij.lang.annotation.ExternalAnnotator#collectInformation}
 * is invoked, which collects some initial information from the IDE to support
 * annotation. This method should return quickly.
 *
 * Then {@link com.intellij.lang.annotation.ExternalAnnotator#doAnnotate}
 * is invoked on the collected information, which is expected to be a long-running
 * annotation pass. It is run in a separate thread.
 *
 * Finally {@link com.intellij.lang.annotation.ExternalAnnotator#apply)} is
 * invoked with the gathered annotations, which should apply them to the source file.
 * This is again expected to be a quick operation.
 */
@NonNullByDefault
package org.metaborg.intellij.idea.parsing.annotations;

import org.metaborg.intellij.NonNullByDefault;