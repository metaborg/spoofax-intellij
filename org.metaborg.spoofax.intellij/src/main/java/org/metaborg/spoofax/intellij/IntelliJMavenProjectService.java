package org.metaborg.spoofax.intellij;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.utils.actions.MavenActionUtil;
import org.metaborg.core.project.IProject;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.core.project.IMavenProjectService;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Maven project service for IntelliJ modules.
 */
@Singleton
public final class IntelliJMavenProjectService implements IMavenProjectService {

    private final IResourceService resourceService;

    @Inject
    private IntelliJMavenProjectService(IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Nullable
    @Override
    public MavenProject get(IProject project) {
        MavenProject mavenProject = tryGetFromProject(project);

        if (mavenProject == null)
            mavenProject = tryGetFromLocation(project.location());

        return mavenProject;
    }

    /**
     * Attempts to get the Maven project from the Spoofax project,
     * if it's an instance of {@link IdeaModule}.
     *
     * @param project The Spoofax project whose Maven project to get.
     * @return The corresponding {@link MavenProject}; or null when not found.
     */
    @Nullable
    private MavenProject tryGetFromProject(IProject project)
    {
        if (!(project instanceof IdeaModule))
            return null;

        //IdeaModule ideaModule = (IdeaModule)project;
        //Module m = ideaModule.module();
        //Project ideaProject = m.getProject();

        //final MavenProjectsManager projectsManager = MavenProjectsManager.getInstance(ideaProject);
        //org.jetbrains.idea.maven.project.MavenProject mavenProject = projectsManager.findProject(m);

        // FIXME THIS?
        //org.apache.maven.project.MavenProject <-> org.jetbrains.idea.maven.project.MavenProject
        return null;
    }

    /**
     * Attempts to get the Maven project from the project's physical location,
     * if it's an instance of {@link IdeaModule}.
     *
     * @param location The location of the Maven project's pom.xml.
     * @return The corresponding {@link MavenProject}; or null when not found.
     */
    @Nullable
    private MavenProject tryGetFromLocation(final FileObject location)
    {
        final FileObject pomResource;
        Model model = null;
        try {
            pomResource = location.resolveFile("pom.xml");
            if (!pomResource.exists())
                return null;

            File pomFile = this.resourceService.localFile(pomResource);

            MavenXpp3Reader mavenReader = new MavenXpp3Reader();
                FileReader reader = new FileReader(pomFile);
            model = mavenReader.read(reader);
            reader.close();
            model.setPomFile(pomFile);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileSystemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MavenProject mavenProject = new MavenProject(model);
        //mavenProject.setFile(pomFile);
        //mavenProject.getBuild().setDirectory(pomFile.getParentFile().getPath());
        //mavenProject.addCompileSourceRoot(pomFile.getParentFile().getPath() + "/src/main/java");
        //mavenProject.addTestCompileSourceRoot(pomFile.getParentFile().getPath() + "/src/test/java");
        return mavenProject;
    }
}
