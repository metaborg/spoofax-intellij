package org.metaborg.spoofax.intellij.jps.ant;

import org.apache.tools.ant.*;
import org.metaborg.spoofax.meta.core.ant.IAntRunner;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.util.Map;

/**
 * Ant runner.
 */
public final class DefaultAntRunner implements IAntRunner {

    private final Project antProject;

    public DefaultAntRunner(File antFile, File baseDir,
                            Map<String, String> properties, @Nullable URL[] classpaths, @Nullable BuildListener listener) {

        this.antProject = new Project();
        this.antProject.setUserProperty("ant.file", antFile.getAbsolutePath());
        properties.put("basedir", baseDir.getPath());
        addUserProperties(this.antProject, properties);
        setCustomClasspath(this.antProject, classpaths);
        this.antProject.init();
        ProjectHelper helper = ProjectHelper.getProjectHelper();
        this.antProject.addReference("ant.projectHelper", helper);
        helper.parse(this.antProject, antFile);

        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
        this.antProject.addBuildListener(consoleLogger);

        if(listener != null) {
            this.antProject.addBuildListener(listener);
        }
    }

    private void addUserProperties(Project antProject, Map<String, String> properties) {
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            antProject.setUserProperty(entry.getKey(), entry.getValue());
        }
    }

    private void setCustomClasspath(Project antProject, URL[] classpaths) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (URL u : classpaths) {
            if (!first)
                sb.append("; ");
            sb.append(u.toString());
            first = false;
        }
        String urlString = sb.toString();
        // TODO: Set classpath
    }

    @Override
    public void execute(String target) throws Exception {
        if (target == null)
            target = this.antProject.getDefaultTarget();
        this.antProject.executeTarget(target);
    }
}
