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
