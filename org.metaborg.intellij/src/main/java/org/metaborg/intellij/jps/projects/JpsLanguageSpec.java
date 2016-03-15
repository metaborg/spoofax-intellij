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

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import org.apache.commons.vfs2.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.module.*;
import org.metaborg.core.project.*;
import org.metaborg.meta.core.config.*;
import org.metaborg.meta.core.project.*;
import org.metaborg.spoofax.meta.core.config.*;
import org.metaborg.spoofax.meta.core.project.*;

import java.util.*;

/**
 * A Spoofax project used in JPS.
 */
public final class JpsLanguageSpec extends MetaborgJpsProject implements ISpoofaxLanguageSpec {

    private final ISpoofaxLanguageSpecConfig config;
    private final ISpoofaxLanguageSpecPaths paths;

    /**
     * Initializes a new instance of the {@link JpsLanguageSpec} class.
     *
     * @param location
     *            The location of the project root.
     */
    @Inject
    public JpsLanguageSpec(@Assisted final JpsModule module,
                           @Assisted final FileObject location,
                           @Assisted final ISpoofaxLanguageSpecConfig config,
                           @Assisted final ISpoofaxLanguageSpecPaths paths) {
        super(module, location, config);
        this.config = config;
        this.paths = paths;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ISpoofaxLanguageSpecConfig config() {
        return this.config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ISpoofaxLanguageSpecPaths paths() {
        return this.paths;
    }
}
