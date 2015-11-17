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

package org.metaborg.spoofax.intellij.project;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProjectService;

import javax.annotation.Nullable;

/**
 * Project service for IntelliJ.
 */
public interface IIntelliJProjectService extends IProjectService {

    /**
     * Indicates to the project service that a Spoofax project was opened.
     *
     * @param project The Spoofax project that was opened.
     */
    void open(@NotNull IntelliJProject project);

    /**
     * Indicates to the project service that a Spoofax project was closed.
     *
     * @param module The IntelliJ module of the project.
     */
    void close(@NotNull Module module);

    /**
     * Retrieves the Spoofax project of a given IntelliJ module.
     *
     * @param module The IntelliJ module.
     * @return The corresponding Spoofax project;
     * or <code>null</code> if no project could be found.
     */
    @Nullable
    IntelliJProject get(Module module);

    /**
     * Retrieves the Spoofax project of a given PSI element.
     *
     * @param element The PSI element.
     * @return The corresponding Spoofax project;
     * or <code>null</code> if no project could be found.
     */
    @Nullable
    IntelliJProject get(PsiElement element);

}
