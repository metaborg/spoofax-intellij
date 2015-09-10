package org.metaborg.spoofax.intellij.resources;

import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractFileName;
import org.apache.commons.vfs2.provider.AbstractFileSystem;

import java.util.Collection;

/**
 * The IntelliJ IDEA file system.
 */
public class IntelliJFileSystem extends AbstractFileSystem {

    public IntelliJFileSystem(FileName rootName, FileObject parentLayer, FileSystemOptions fileSystemOptions) {
        super(rootName, parentLayer, fileSystemOptions);
    }

    @Override
    protected FileObject createFile(AbstractFileName name) throws Exception {
        return new IntelliJFileObject(name, this);
    }

    @Override
    protected void addCapabilities(Collection<Capability> collection) {
        collection.addAll(IntelliJResourceProvider.capabilities);
    }

    /*
    @Override
    protected File doReplicateFile(FileObject file, FileSelector selector) throws Exception {
        final IntelliJFileObject intellijResource = (IntelliJFileObject) file;
        final VirtualFile resource = intellijResource.getIntelliJFile();
        if(resource == null) {
            return super.doReplicateFile(file, selector);
        }
        IPath path = resource.getRawLocation();
        if(path == null) {
            path = resource.getLocation();
        }
        if(path == null) {
            return super.doReplicateFile(file, selector);
        }
        return path.makeAbsolute().toFile();
    }*/
}
