/*
 * Copyright © 2015-2015
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.spoofax.intellij.project.settings;

//import com.google.inject.Inject;
//import groovy.lang.Singleton;
//import org.jetbrains.annotations.NotNull;
//import org.metaborg.core.project.IProject;
//import org.metaborg.core.project.ProjectException;
//import org.metaborg.core.project.settings.IProjectSettings;
//import org.metaborg.spoofax.core.project.settings.Format;
//import org.metaborg.spoofax.core.project.settings.ISpoofaxProjectSettingsService;
//import org.metaborg.spoofax.core.project.settings.SpoofaxProjectSettings;

///**
// * Spoofax project settings service for IntelliJ.
// */
//@Deprecated
//@Singleton
//public final class IntelliJSpoofaxProjectSettingsService implements ISpoofaxProjectSettingsService {
//
//    @NotNull
//    private final IProjectSettingsService2 projectSettingsService;
//
//    @Inject
//    /* package private */ IntelliJSpoofaxProjectSettingsService(@NotNull final IProjectSettingsService2 projectSettingsService) {
//        this.projectSettingsService = projectSettingsService;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public SpoofaxProjectSettings get(@NotNull final IProject project) throws ProjectException {
//        final IProjectSettings settings = this.projectSettingsService.get(project);
//        final SpoofaxProjectSettings spoofaxSettings = new SpoofaxProjectSettings(settings, project.location());
//
//        // TODO: Read these special settings:
//        Format format = Format.jar;
////        List<String> pardonedLanguages;
////        List<String> sdfArgs;
////        List<String> strategoArgs;
////        String externalDef;
////        String externalJar;
////        String externalJarFlags;
//
//        // TODO: And add them to the settings.
//        spoofaxSettings.setFormat(format);
////        spoofaxSettings.setPardonedLanguages(pardonedLanguages);
////        spoofaxSettings.setSdfArgs(sdfArgs);
////        spoofaxSettings.setStrategoArgs(strategoArgs);
////        spoofaxSettings.setExternalDef(externalDef);
////        spoofaxSettings.setExternalJar(externalJar);
////        spoofaxSettings.setExternalJarFlags(externalJarFlags);
//
//        return spoofaxSettings;
//    }
//}
