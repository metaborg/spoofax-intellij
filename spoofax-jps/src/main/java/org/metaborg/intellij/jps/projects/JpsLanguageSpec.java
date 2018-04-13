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

package org.metaborg.intellij.jps.projects;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.spoofax.meta.core.config.ISpoofaxLanguageSpecConfig;
import org.metaborg.spoofax.meta.core.project.ISpoofaxLanguageSpec;

/**
 * A Spoofax project used in JPS.
 */
public final class JpsLanguageSpec extends MetaborgJpsProject implements ISpoofaxLanguageSpec {

    private final ISpoofaxLanguageSpecConfig config;

    /**
     * Initializes a new instance of the {@link JpsLanguageSpec} class.
     *
     * @param location
     *            The location of the project root.
     */
    @Inject
    public JpsLanguageSpec(@Assisted final JpsModule module,
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
