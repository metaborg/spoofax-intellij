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

package org.metaborg.intellij.idea.filetypes;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.files.SpoofaxLanguageArtifactFileType;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import java.util.List;

///**
// * File type factory for file types implementing the {@link IMetaborgFileType} interface.
// */
//@Deprecated
//public final class SpoofaxLanguageArtifactFileTypeFactoryX extends FileTypeFactory {
//
//    @InjectLogger
//    private ILogger logger;
//    private SpoofaxLanguageArtifactFileType artifactFileType;
//
//    /**
//     * This instance is created by IntelliJ's plugin system.
//     * Do not call this constructor manually.
//     */
//    public SpoofaxLanguageArtifactFileTypeFactoryX() {
//        super();
//        SpoofaxIdeaPlugin.injector().injectMembers(this);
//    }
//
//    @Inject
//    @SuppressWarnings("unused")
//    private void inject(final SpoofaxLanguageArtifactFileType artifactFileType) {
//        this.artifactFileType = artifactFileType;
//    }
//
//    @Override
//    public void createFileTypes(final FileTypeConsumer consumer) {
//        createFileTypes(new SpoofaxLanguageArtifactFileTypeConsumer(consumer));
//    }
//
//    /**
//     * Lets the consumer consume all new file types and their associated matchers or extensions.
//     *
//     * @param consumer The consumer.
//     */
//    private void createFileTypes(final SpoofaxLanguageArtifactFileTypeConsumer consumer) {
//        this.logger.debug("Registering file type: {}", this.artifactFileType);
//        consumer.consume(this.artifactFileType);
//        this.logger.info("Registered file type: {}", this.artifactFileType);
//    }
//
//}
