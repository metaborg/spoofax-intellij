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

package org.metaborg.intellij.idea.projects;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.meta.core.project.*;

import java.util.*;

/**
 * An IntelliJ IDEA language specification project.
 */
public class IdeaLanguageSpecProject extends IdeaProject implements ILanguageSpec {

    private Collection<ILanguageComponent> components = Collections.emptyList();

    /**
     * Gets the language components that are contributed by this language specification.
     *
     * @return The contributed language components.
     */
    public Collection<ILanguageComponent> getComponents() {
        return this.components;
    }

    /**
     * Gets the language components that are contributed by this language specification.
     *
     * @param components The contributed language components.
     */
    public void setComponents(Collection<ILanguageComponent> components) {
        this.components = components;
    }

    @Inject
    /* package private */ IdeaLanguageSpecProject(
            @Assisted final Module module,
            @Assisted final FileObject location) {
        super(module, location);
    }
}
