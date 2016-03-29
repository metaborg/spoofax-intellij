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

package org.metaborg.intellij.idea.projects;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.spoofax.meta.core.config.ISpoofaxLanguageSpecConfig;
import org.metaborg.spoofax.meta.core.project.ISpoofaxLanguageSpec;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.openapi.module.Module;

/**
 * An IntelliJ IDEA language specification project.
 */
public class IdeaLanguageSpec extends IdeaProject implements ISpoofaxLanguageSpec {

    private final ISpoofaxLanguageSpecConfig config;
    
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
    public void setComponents(final Collection<ILanguageComponent> components) {
        this.components = components;
    }

    @Inject
    /* package private */ IdeaLanguageSpec(
            @Assisted final Module module,
            @Assisted final FileObject location,
            @Assisted final ISpoofaxLanguageSpecConfig config) {
        super(module, location, config);
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ISpoofaxLanguageSpecConfig config() {
        return this.config;
    }
}
