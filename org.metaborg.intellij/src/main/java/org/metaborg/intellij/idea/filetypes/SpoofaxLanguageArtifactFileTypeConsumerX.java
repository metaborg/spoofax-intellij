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

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.virtlink.editorservices.intellij.files.IFileTypeExt;
import org.jetbrains.annotations.NonNls;

/**
 * Consumer for file types implementing the {@link IMetaborgFileType} interface.
 */
@Deprecated
public final class SpoofaxLanguageArtifactFileTypeConsumerX {

    private final FileTypeConsumer consumer;

    /**
     * Initializes a new instance of the {@link SpoofaxLanguageArtifactFileTypeConsumer} class.
     *
     * @param consumer The file type consumer to wrap.
     */
    public SpoofaxLanguageArtifactFileTypeConsumerX(final FileTypeConsumer consumer) {
        Preconditions.checkNotNull(consumer);

        this.consumer = consumer;
    }

    /**
     * Consumes the file type with its default extensions.
     *
     * @param fileType The file type.
     */
    public void consume(final IFileTypeExt fileType) {
        Preconditions.checkNotNull(fileType);

        consume(fileType, Iterables.toArray(fileType.getExtensions(), String.class));
    }

    /**
     * Consumes the file type with only the specified extensions.
     *
     * @param fileType   The file type.
     * @param extensions The file extensions, without a leading '.'.
     */
    public void consume(final FileType fileType, @NonNls final String... extensions) {
        Preconditions.checkNotNull(fileType);
        Preconditions.checkNotNull(extensions);

        final FileNameMatcher[] matchers = new FileNameMatcher[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            matchers[i] = new ExtensionFileNameMatcher(extensions[i]);
        }
        consume(fileType, matchers);
    }

    /**
     * Consumes the file type with only the specified file name matchers.
     *
     * @param fileType The file type.
     * @param matchers The file name matchers.
     */
    public void consume(final FileType fileType, final FileNameMatcher... matchers) {
        Preconditions.checkNotNull(fileType);
        Preconditions.checkNotNull(matchers);

        this.consumer.consume(fileType, matchers);
    }

}
