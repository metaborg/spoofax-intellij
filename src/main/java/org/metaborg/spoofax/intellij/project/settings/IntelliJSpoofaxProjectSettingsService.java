package org.metaborg.spoofax.intellij.project.settings;

import com.google.inject.Inject;
import groovy.lang.Singleton;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.project.IProject;
import org.metaborg.core.project.ProjectException;
import org.metaborg.core.project.settings.IProjectSettings;
import org.metaborg.spoofax.core.project.settings.ISpoofaxProjectSettingsService;
import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;

@Singleton
public final class IntelliJSpoofaxProjectSettingsService implements ISpoofaxProjectSettingsService {

    @NotNull
    private final IProjectSettingsService2 projectSettingsService;

    @Inject
    private IntelliJSpoofaxProjectSettingsService(@NotNull final IProjectSettingsService2 projectSettingsService) {
        this.projectSettingsService = projectSettingsService;
    }

    @Override
    public SpoofaxProjectSettings get(@NotNull final IProject project) throws ProjectException {
        final IProjectSettings settings = this.projectSettingsService.get(project);
        final SpoofaxProjectSettings spoofaxSettings = new SpoofaxProjectSettings(settings, project.location());

        // TODO: Read these special settings:
//        Format format;
//        List<String> pardonedLanguages;
//        List<String> sdfArgs;
//        List<String> strategoArgs;
//        String externalDef;
//        String externalJar;
//        String externalJarFlags;

        // TODO: And add them to the settings.
//        spoofaxSettings.setFormat(format);
//        spoofaxSettings.setPardonedLanguages(pardonedLanguages);
//        spoofaxSettings.setSdfArgs(sdfArgs);
//        spoofaxSettings.setStrategoArgs(strategoArgs);
//        spoofaxSettings.setExternalDef(externalDef);
//        spoofaxSettings.setExternalJar(externalJar);
//        spoofaxSettings.setExternalJarFlags(externalJarFlags);

        return spoofaxSettings;
    }
}
