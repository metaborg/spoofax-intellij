package org.metaborg.spoofax.intellij.resources;

import org.metaborg.core.resource.DefaultFileSystemManagerProvider;

/**
 * Installs the <code>idea://</code> URI schema in the VFS.
 */
public final class IntelliJFileSystemManagerProvider extends DefaultFileSystemManagerProvider {

    // NOTE: You ideally don't want to copy the `addProvider "file"` just because you changed the default provider.
    // Perhaps it's cleaner to add all providers in `addProviders`, and then return the provider you want to be
    // the default. If you don't want to change the default, just `return super.addProviders(manager)`.

    // FIXME: The `idea://` provider was the default, but this caused issues:
    // org.apache.commons.vfs2.FileSystemException: Could not determine the type of file "idea:///home/daniel/eclipse/spoofax1507/workspace/TestProject/untitled20/pom.xml".
    // Therefore, I used the default `file://` provider, and just added `idea://` without making it the default.

}