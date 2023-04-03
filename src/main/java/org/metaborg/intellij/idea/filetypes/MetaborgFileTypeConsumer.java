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

import java.util.Collection;
import java.util.stream.Collectors;

import org.metaborg.util.iterators.Iterables2;

import com.intellij.openapi.fileTypes.ExtensionFileNameMatcher;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;

/**
 * Consumer for file types implementing the {@link IMetaborgFileType} interface.
 */
public final class MetaborgFileTypeConsumer {

    private final FileTypeConsumer consumer;

    /**
     * Initializes a new instance of the {@link MetaborgFileTypeConsumer} class.
     *
     * @param consumer The file type consumer to wrap.
     */
    public MetaborgFileTypeConsumer(final FileTypeConsumer consumer) {
        if (consumer == null) {
          throw new NullPointerException();
        }

        this.consumer = consumer;
    }

    /**
     * Consumes the file type with its default extensions.
     *
     * @param fileType The file type.
     */
    public void consume(final IMetaborgFileType fileType) {
        if (fileType == null) {
          throw new NullPointerException();
        }

        consume(fileType, fileType.getExtensions());
    }

    /**
     * Consumes the file type with only the specified extensions.
     *
     * @param fileType   The file type.
     * @param extensions The file extensions, without a leading '.'.
     */
    public void consume(final FileType fileType, final Iterable<String> extensions) {
        if (fileType == null) {
          throw new NullPointerException();
        }
        if (extensions == null) {
          throw new NullPointerException();
        }

        consume(fileType, Iterables2.stream(extensions).map(ExtensionFileNameMatcher::new)
            .collect(Collectors.toList()));
    }

    /**
     * Consumes the file type with only the specified file name matchers.
     *
     * @param fileType The file type.
     * @param matchers The file name matchers.
     */
    public void consume(final FileType fileType, final Collection<FileNameMatcher> matchers) {
        if (fileType == null) {
          throw new NullPointerException();
        }
        if (matchers == null) {
          throw new NullPointerException();
        }

        this.consumer.consume(fileType, matchers.toArray(new FileNameMatcher[0]));
    }

}
