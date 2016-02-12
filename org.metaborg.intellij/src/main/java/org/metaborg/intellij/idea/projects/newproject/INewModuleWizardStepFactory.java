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

package org.metaborg.intellij.idea.projects.newproject;

import com.google.inject.assistedinject.*;
import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.module.*;
import org.apache.commons.vfs2.*;
import org.metaborg.intellij.idea.projects.*;

/**
 * Factory for new module wizard steps.
 */
public interface INewModuleWizardStepFactory {

    /**
     * Creates a new wizard step.
     *
     * @param builder The module builder.
     * @param context The wizard context.
     * @return The wizard step.
     */
    MetaborgNewModuleWizardStep create(MetaborgModuleBuilder builder, WizardContext context);

}
