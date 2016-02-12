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

import org.metaborg.intellij.*;