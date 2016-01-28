/*
 * Copyright © 2015-2015
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

package org.metaborg.intellij.idea.psi;

import com.intellij.codeInsight.completion.CompletionContributor;

/**
 * Contributes completion providers for a language.
 *
 * This completion provider must be registered for each language.
 */
public class MetaborgCompletionContributor extends CompletionContributor {
    public MetaborgCompletionContributor() {
        // TODO
        // See: https://github.com/pantsbuild/intellij-pants-plugin/blob/9e72634923841169c9df5cd683e54335d1af5835/src/com/twitter/intellij/pants/completion/PantsCompletionContributor.java
    }
}
