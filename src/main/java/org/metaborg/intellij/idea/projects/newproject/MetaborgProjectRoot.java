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

package org.metaborg.intellij.idea.projects.newproject;

import com.intellij.ide.util.projectWizard.importSources.DetectedProjectRoot;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A detected Metaborg project's root.
 */
public final class MetaborgProjectRoot extends DetectedProjectRoot {

    /**
     * Initializes a new instance of the {@link MetaborgProjectRoot} class.
     *
     * @param directory The root directory.
     */
    public MetaborgProjectRoot(final File directory) {
        super(directory);
    }

    @Override
    public boolean canContainRoot(@NotNull DetectedProjectRoot root) {
        // Prevent the Java DetectedSourceRoot (or any other root for that matter)
        // to appear _under_ this project.
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRootTypeName() {
        return "Spoofax language";
    }
}
