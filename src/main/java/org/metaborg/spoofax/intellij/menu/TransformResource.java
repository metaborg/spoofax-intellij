package org.metaborg.spoofax.intellij.menu;

import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;

/**
 * A tuple with a resource to transform, along with the (part of its) source code to transform.
 */
public final class TransformResource {

    @NotNull
    private final FileObject resource;
    @NotNull
    private final String text;

    /**
     * Initializes a new instance of the {@link TransformResource} class.
     *
     * @param resource The resource to transform.
     * @param text The text to transform.
     */
    public TransformResource(@NotNull final FileObject resource, @NotNull final String text) {
        this.resource = resource;
        this.text = text;
    }

    /**
     * Gets the resource to transform.
     *
     * @return The resource to transform.
     */
    @NotNull
    public FileObject resource() {
        return this.resource;
    }

    /**
     * Gets the text to transform.
     *
     * @return The text to transform.
     */
    @NotNull
    public String text() {
        return this.text;
    }
}
