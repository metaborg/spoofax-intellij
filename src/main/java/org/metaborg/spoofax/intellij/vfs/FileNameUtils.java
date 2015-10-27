package org.metaborg.spoofax.intellij.vfs;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.provider.LayeredFileName;

import javax.annotation.Nullable;

/**
 * Utility functions for working with Apache {@link FileName} objects.
 */
public final class FileNameUtils {
    // To prevent instantiation.
    private FileNameUtils() {}

    /**
     * Gets the outer file name of the specified file name.
     *
     * For example, if the specified file name is <code>zip:file:///dir/archive.zip!/document.txt</code>,
     * then the outer file name is <code>file:///dir/archive.zip</code>.
     *
     * @param fileName The file name.
     * @return The outer file name; or <code>null</code> when there is none.
     */
    @Nullable
    public static FileName getOuterFileName(FileName fileName) {
        if (fileName instanceof LayeredFileName) {
            return ((LayeredFileName) fileName).getOuterName();
        } else {
            return null;
        }
    }
}
