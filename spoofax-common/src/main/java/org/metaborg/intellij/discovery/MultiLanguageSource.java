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

package org.metaborg.intellij.discovery;


import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.*;
import org.metaborg.intellij.Compound;

import jakarta.annotation.Nullable;
import java.util.Set;

/**
 * Manages multiple language sources.
 *
 * This implementation is thread-safe.
 */
public final class MultiLanguageSource implements ILanguageSource {

    // TODO: Add a way to prioritize some sources above others.
    // E.g. a local source may be preferred above a remote source.

    private final Set<ILanguageSource> sources;

    /**
     * Initializes a new instance of the {@link MultiLanguageSource} class.
     * @param sources The sources.
     */
    @jakarta.inject.Inject
    public MultiLanguageSource(@Compound final Set<ILanguageSource> sources) {
        this.sources = sources;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public FileObject find(final LanguageIdentifier id) {
        // The returned iterator is a snapshot of the state of the list at the moment
        // the iterator was created. Any subsequent changes are not reflected in the iterator,
        // just the way we want it.
        for (final ILanguageSource source : this.sources) {
            @Nullable final FileObject root = source.find(id);
            if (root != null) {
                return root;
            }
        }
        return null;
    }
}
