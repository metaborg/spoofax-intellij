package org.metaborg.spoofax.intellij.jps.targetbuilders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.tools.ant.BuildListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.jetbrains.jps.model.module.JpsModule;
import org.metaborg.core.project.ProjectException;
import org.metaborg.spoofax.core.project.settings.ISpoofaxProjectSettingsService;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;
import org.metaborg.spoofax.intellij.SpoofaxSourceRootDescriptor;
import org.metaborg.spoofax.intellij.jps.project.JpsProjectService;
import org.metaborg.spoofax.intellij.jps.project.SpoofaxJpsProject;
import org.metaborg.spoofax.meta.core.MetaBuildInput;
import org.metaborg.spoofax.meta.core.SpoofaxMetaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Singleton
public final class SpoofaxPostBuilder extends TargetBuilder<SpoofaxSourceRootDescriptor, SpoofaxPostTarget> {

    private static final Logger logger = LoggerFactory.getLogger(SpoofaxPreBuilder.class);

    private final ISpoofaxProjectSettingsService settingsService;
    private final SpoofaxMetaBuilder builder;
    private final JpsProjectService projectService;

    @Inject
    public SpoofaxPostBuilder(SpoofaxPostTargetType targetType, ISpoofaxProjectSettingsService settingsService, SpoofaxMetaBuilder builder, JpsProjectService projectService){
        super(Collections.singletonList(targetType));
        this.settingsService = settingsService;
        this.builder = builder;
        this.projectService = projectService;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Spoofax post-Java builder";
    }

    @Override
    public void build(@NotNull SpoofaxPostTarget target,
                      @NotNull DirtyFilesHolder<SpoofaxSourceRootDescriptor, SpoofaxPostTarget> holder,
                      @NotNull BuildOutputConsumer consumer,
                      @NotNull CompileContext context) throws ProjectBuildException, IOException {

        try {
            final SpoofaxJpsProject project = projectService.get(target.getModule());
            final SpoofaxProjectSettings settings = settingsService.get(project);
            final MetaBuildInput input = new MetaBuildInput(project, settings);

            compilePostJava(input, null, null, context);

        } catch (FileSystemException e) {
            logger.error("An unexpected IO exception occurred.", e);
            throw e;
        } catch (ProjectException e) {
            logger.error("An unexpected project exception occurred.", e);
            throw new ProjectBuildException(e);
        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new ProjectBuildException(e);
        }

    }

    private void compilePostJava(@NotNull MetaBuildInput input, @Nullable URL[] classpath, @Nullable BuildListener listener, @NotNull CompileContext context) throws Exception, ProjectBuildException {
        context.checkCanceled();
        context.processMessage(BuilderUtils.formatProgress(0f, "Packaging language project {}", input.project));
        this.builder.compilePostJava(input, classpath, listener);
    }


}