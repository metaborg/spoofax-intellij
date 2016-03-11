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

package org.metaborg.intellij.jps.builders;

import org.jetbrains.jps.builders.*;

import java.io.*;

/**
 * Describes a source root for the Spoofax build target.
 */
public final class SpoofaxSourceRootDescriptor extends BuildRootDescriptor {

    private final File root;
    private final BuildTarget<?> target;

    /**
     * Initializes a new instance of the {@link SpoofaxSourceRootDescriptor} class.
     *
     * @param root   The directory of the source root.
     * @param target The build target to which the source root belongs.
     */
    public SpoofaxSourceRootDescriptor(final File root, final BuildTarget<?> target) {
        super();
        this.root = root;
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getRootId() {
        return this.root.getAbsolutePath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final File getRootFile() {
        return this.root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final BuildTarget<?> getTarget() {
        return this.target;
    }

}