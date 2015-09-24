package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.apache.tools.ant.BuildListener;
import org.metaborg.spoofax.meta.core.ant.IAntRunner;
import org.metaborg.spoofax.meta.core.ant.IAntRunnerService;

import java.net.URL;
import java.util.Map;

@Singleton
public final class IntelliJAntRunnerService implements IAntRunnerService {
    @Override
    public IAntRunner get(FileObject antFile, FileObject baseDir, Map<String, String> properties, URL[] classpaths, BuildListener listener) {
        return null;
    }
}
