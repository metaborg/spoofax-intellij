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

package org.metaborg.intellij.idea.discovery;

import com.google.inject.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.projects.*;

import javax.annotation.*;
import java.util.*;

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
    @Inject
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
