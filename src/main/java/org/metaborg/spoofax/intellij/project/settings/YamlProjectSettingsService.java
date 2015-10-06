package org.metaborg.spoofax.intellij.project.settings;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.settings.IProjectSettings;
import org.metaborg.core.project.settings.YAMLProjectSettingsSerializer;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * A project settings service that stores the settings in a YAML file.
 */
@Singleton
public final class YamlProjectSettingsService implements IProjectSettingsService2 {

    /**
     * Path to the project settings file, relative to the project's root.
     */
    @NotNull
    private static final String SETTINGS_FILE = "spoofax-project.yaml";

    //@NotNull
    //private static final Logger logger = LoggerFactory.getLogger(IntelliJProjectSettingsService.class);
    @NotNull
    private final IResourceService resourceService;
    @InjectLogger
    private Logger logger;

    @Inject
    private YamlProjectSettingsService(@NotNull final IResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    @NotNull
    public final IProjectSettings create() {
        final String url = this.getClass().getClassLoader().getResource("defaultsettings.yaml").toString();
        final FileObject defaultSettings = this.resourceService.resolve(url);

        try {
            return YAMLProjectSettingsSerializer.read(defaultSettings);
        } catch (IOException e) {
            logger.warn(String.format("Reading default settings file failed unexpectedly: %s", defaultSettings), e);
            return create();
        }
    }

    @Override
    @NotNull
    public IProjectSettings get(@NotNull final IProject project) {
        final FileObject location = project.location();
        try {
            final FileObject settingsFile = location.resolveFile(SETTINGS_FILE);
            if (!settingsFile.exists()) {
                return create();
            }
            final IProjectSettings settings = YAMLProjectSettingsSerializer.read(settingsFile);
            return settings;
        } catch (IOException e) {
            logger.warn(String.format("Reading settings file failed unexpectedly: %s/%s", location, SETTINGS_FILE), e);
            return create();
        }
    }

    @Override
    @NotNull
    public void set(@NotNull final IProject project, @NotNull final IProjectSettings settings) {
        final FileObject location = project.location();
        try {
            final FileObject settingsFile = location.resolveFile(SETTINGS_FILE);
            YAMLProjectSettingsSerializer.write(settingsFile, settings);
        } catch (IOException e) {
            logger.warn(String.format("Writing settings file failed unexpectedly: %s/%s", location, SETTINGS_FILE), e);
        }
    }
}
