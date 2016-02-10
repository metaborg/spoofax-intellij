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

package org.metaborg.intellij.idea.filetypes;

import com.google.common.collect.*;
import com.google.inject.*;
import com.intellij.openapi.fileTypes.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import java.util.*;

/**
 * File type factory for file types implementing the {@link IMetaborgFileType} interface.
 */
public final class MetaborgFileTypeFactory extends FileTypeFactory {

    @InjectLogger
    private ILogger logger;
    private LanguageArtifactFileType artifactFileType;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public MetaborgFileTypeFactory() {
        super();
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    // TODO: Maybe multi-inject a Set and register them all?
    @Inject
    @SuppressWarnings("unused")
    private void inject(final LanguageArtifactFileType artifactFileType) {
        this.artifactFileType = artifactFileType;
    }

    @Override
    public void createFileTypes(final FileTypeConsumer consumer) {
        createFileTypes(new MetaborgFileTypeConsumer(consumer));
    }

    /**
     * Lets the consumer consume all new file types and their associated matchers or extensions.
     *
     * @param consumer The consumer.
     */
    public void createFileTypes(final MetaborgFileTypeConsumer consumer) {
        final List<IMetaborgFileType> fileTypes = Lists.newArrayList(this.artifactFileType);
        for (final IMetaborgFileType fileType : fileTypes) {
            this.logger.debug("Registering file type: {}", fileType);
            consumer.consume(fileType);
            this.logger.info("Registered file type: {}", fileType);
        }
    }

}
