package org.metaborg.spoofax.intellij.jps.builders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileSystemException;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.ProjectException;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettingsService;
import org.metaborg.spoofax.meta.core.MetaBuildInput;
import org.metaborg.spoofax.meta.core.SpoofaxMetaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class InitializationBuildStep extends BuildStep {

    @Singleton
    public final static class InitializationBuildStepDescriptor implements IBuildStepDescriptor {

        private final SpoofaxMetaBuilder metaBuilder;
        private final SpoofaxProjectSettingsService settingsService;

        @Inject
        private InitializationBuildStepDescriptor(SpoofaxMetaBuilder metaBuilder, SpoofaxProjectSettingsService settingsService) {
            this.metaBuilder = metaBuilder;
            this.settingsService = settingsService;
        }

        @Override
        public String name() {
            return "Spoofax init";
        }

        @Override
        public List<IBuildStepDescriptor> dependencies() {
            return Collections.emptyList();
        }

        @Override
        public IBuildStep createStep(IProject project) {
            return new InitializationBuildStep(project, this, metaBuilder, settingsService);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(InitializationBuildStep.class);

    private final SpoofaxMetaBuilder metaBuilder;
    private final SpoofaxProjectSettingsService settingsService;

    @Inject
    private InitializationBuildStep(IProject project, InitializationBuildStepDescriptor descriptor, SpoofaxMetaBuilder metaBuilder, SpoofaxProjectSettingsService settingsService) {
        super(descriptor, project);
        this.metaBuilder = metaBuilder;
        this.settingsService = settingsService;
    }

    @Override
    public void onBuild() {
        MetaBuildInput input = getInput();
        try {
            this.metaBuilder.initialize(input);
        } catch (FileSystemException e) {
            log.error("Unhandled exception.", e);
        }
    }

    private MetaBuildInput getInput() {
        SpoofaxProjectSettings settings = null;
        try {
            settings = settingsService.get(this.project());
        } catch (ProjectException e) {
            log.error("Unhandled exception.", e);
        }
        return new MetaBuildInput(this.project(), settings);
    }
}
