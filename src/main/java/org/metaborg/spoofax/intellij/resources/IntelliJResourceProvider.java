package org.metaborg.spoofax.intellij.resources;


import com.google.common.collect.ImmutableList;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;

import java.util.Collection;

/**
 * Provides the resources in the <code>idea://</code> VFS.
 */
public class IntelliJResourceProvider extends AbstractOriginatingFileProvider {

    // @formatter:off
    public static final Collection<Capability> capabilities = ImmutableList.of(
            Capability.CREATE,
            Capability.DELETE,
            Capability.RENAME,
            Capability.GET_TYPE,
            Capability.GET_LAST_MODIFIED,
            Capability.SET_LAST_MODIFIED_FILE,
            Capability.SET_LAST_MODIFIED_FOLDER,
            Capability.LIST_CHILDREN,
            Capability.READ_CONTENT,
            Capability.URI,
            Capability.WRITE_CONTENT,
            Capability.APPEND_CONTENT,
            Capability.RANDOM_ACCESS_READ,
            Capability.RANDOM_ACCESS_WRITE
    );
    // @formatter:on

    @Override
    public Collection<Capability> getCapabilities() {
        return capabilities;
    }

    @Override
    protected FileSystem doCreateFileSystem(FileName rootName, FileSystemOptions fileSystemOptions)
            throws FileSystemException {
        return new IntelliJFileSystem(rootName, null, fileSystemOptions);
    }

}