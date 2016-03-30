/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.metaborg.intellij.discovery;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.annotation.Nullable;

import org.apache.commons.vfs2.FileObject;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.intellij.idea.languages.LanguageUtils2;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.ILogger;

import com.google.inject.Inject;

/**
 * Looks in the <code>languages/</code> resources folder for a language.
 */
public final class ResourceLanguageSource implements ILanguageSource {
    private final IResourceService resourceService;
    @InjectLogger private ILogger logger;


    @Inject public ResourceLanguageSource(IResourceService resourceService) {
        this.resourceService = resourceService;
    }


    @Override public @Nullable FileObject find(LanguageIdentifier id) {
        final String path = getPath(id);
        final @Nullable URL url = getClass().getClassLoader().getResource(path);
        if(url == null) {
            logger.info("Meta language '{}' could not be found in resource: {}", id, path);
            return null;
        }
        final URI uri;
        try {
            uri = url.toURI();
        } catch(URISyntaxException e) {
            logger.info("Meta language '{}' was found at {}, but has an invalid URI syntax", e, id, path);
            return null;
        }
        final FileObject artifact = resourceService.resolve(uri);
        return resourceService.resolve(LanguageUtils2.getArtifactUri(artifact));
    }


    private String getPath(final LanguageIdentifier id) {
        return "languages/" + id.id + "-" + id.version + ".spoofax-language";
    }
}
