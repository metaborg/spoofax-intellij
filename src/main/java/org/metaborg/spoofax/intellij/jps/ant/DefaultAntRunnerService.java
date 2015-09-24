package org.metaborg.spoofax.intellij.jps.ant;

import com.google.inject.Inject;
import org.apache.commons.vfs2.FileObject;
import org.apache.tools.ant.BuildListener;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.meta.core.ant.IAntRunner;
import org.metaborg.spoofax.meta.core.ant.IAntRunnerService;

import java.io.File;
import java.net.URL;
import java.util.Map;

public final class DefaultAntRunnerService implements IAntRunnerService {

    private final IResourceService resourceService;

    @Inject
    private DefaultAntRunnerService(@NotNull IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public IAntRunner get(FileObject antFile, FileObject baseDir, Map<String, String> properties, URL[] classpaths, BuildListener listener) {
        final File localAntFile = this.resourceService.localFile(antFile);
        final File localBaseDir = this.resourceService.localPath(baseDir);

        return new DefaultAntRunner(localAntFile, localBaseDir, properties, classpaths, listener);
    }
}
