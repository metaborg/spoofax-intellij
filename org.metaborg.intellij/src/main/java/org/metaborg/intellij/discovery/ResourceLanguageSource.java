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

package org.metaborg.intellij.discovery;

import com.google.inject.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.language.*;
import org.metaborg.core.resource.*;
import org.metaborg.intellij.idea.languages.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import javax.annotation.*;
import java.net.*;

/**
 * Looks in the <code>languages/</code> resources folder for a language.
 */
public final class ResourceLanguageSource implements ILanguageSource {

    private final IResourceService resourceService;
    @InjectLogger
    private ILogger logger;

    /**
     * Initializes a new instance of the {@link ResourceLanguageSource} class.
     *
     * @param resourceService The resource service.
     */
    @Inject
    public ResourceLanguageSource(final IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public FileObject find(final LanguageIdentifier id) {
        final String path = getPath(id);
        @Nullable final URL url = this.getClass().getClassLoader().getResource(path);
        if (url == null) {
            this.logger.info("Meta language '{}' could not be found in resource: {}", id, path);
            return null;
        }
        final FileObject artifact = this.resourceService.resolve(url.getPath());
        return this.resourceService.resolve(LanguageUtils2.getArtifactUri(artifact));
    }

    /**
     * Gets the expected filename of the language artifact with the specified identifier.
     * @param id The identifier.
     * @return The expected filename.
     */
    private String getPath(final LanguageIdentifier id) {
        return "languages/" + id.id + "-" + id.version + ".spoofax-language";
    }
}
