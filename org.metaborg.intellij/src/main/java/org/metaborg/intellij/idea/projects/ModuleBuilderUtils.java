/*
 * Copyright © 2015-2016
 *
 * This file is part of org.metaborg.intellij.
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

package org.metaborg.intellij.idea.projects;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.util.Pair;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.*;
import org.metaborg.spoofax.meta.core.build.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions.
 */
public class ModuleBuilderUtils {

    /**
     * Gets a list of source paths.
     *
     * @return A list of (path, packagePrefix) pairs.
     * @throws ConfigurationException
     */
    @Nullable
    public static List<Pair<String, String>> getSourcePaths(final LanguageIdentifier languageIdentifier,
                                                            final FileObject contentEntry)
            throws ConfigurationException {
        final LangSpecCommonPaths paths = new LangSpecCommonPaths(contentEntry);
        final List<Pair<String, String>> sourcePaths = new ArrayList<>();
        for (final FileObject javaSrcDir : paths.javaSrcDirs(languageIdentifier.id)) {
            sourcePaths.add(Pair.create(javaSrcDir.toString(), ""));
        }
        return sourcePaths;
    }

    @Nullable
    public static void addSourceRoots(final ContentEntry contentEntry,
                                         @Nullable final List<Pair<String, String>> sourcePaths)
            throws ConfigurationException {

        if(sourcePaths == null)
            return;

        for(final Pair<String, String> sourcePath : sourcePaths) {
            final String first = sourcePath.first;
            assert sourcePath.second.equals("") : "Package prefixes are not supported here.";
            contentEntry.addSourceFolder(first, false);
        }
    }

    public static void excludeRoots(final ContentEntry contentEntry, final FileObject root) {
        final LangSpecCommonPaths paths = new LangSpecCommonPaths(root);
        // TODO: Remove unnecessary folders:
        contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + ".idea");
        contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + ".mvn");
        contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + ".cache");
        contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + "lib");
        contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + "include");
        contentEntry.addExcludeFolder(contentEntry.getUrl() + File.separator + "target");
        contentEntry.addExcludeFolder(paths.strCacheDir().toString());
//        contentEntry.addExcludeFolder(paths.srcGenDir().toString());
    }

}
