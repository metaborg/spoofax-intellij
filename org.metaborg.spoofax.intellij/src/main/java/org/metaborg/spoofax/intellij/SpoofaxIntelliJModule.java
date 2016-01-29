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

package org.metaborg.spoofax.intellij;

import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;
import org.apache.commons.vfs2.FileSystemManager;
import org.metaborg.core.language.ILanguageProjectService;
import org.metaborg.core.language.LanguageProjectService;
import org.metaborg.core.logging.Slf4JTypeListener;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.core.SpoofaxModule;
import org.metaborg.spoofax.intellij.languages.LanguageManager;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.metaborg.spoofax.intellij.resources.IntelliJFileSystemManagerProvider;
import org.metaborg.spoofax.intellij.resources.IntelliJResourceService;

/**
 * The Guice dependency injection module for the Spoofax IntelliJ plugins.
 */
public abstract class SpoofaxIntelliJModule extends SpoofaxModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bind(LanguageManager.class).in(Singleton.class);

        bindListener(Matchers.any(), new Slf4JTypeListener());

        bind(ILanguageProjectService.class).to(LanguageProjectService.class).in(Singleton.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindResource() {
        bind(IntelliJResourceService.class).in(Singleton.class);
        bind(IResourceService.class).to(IntelliJResourceService.class).in(Singleton.class);
        bind(IIntelliJResourceService.class).to(IntelliJResourceService.class).in(Singleton.class);
        bind(FileSystemManager.class).toProvider(IntelliJFileSystemManagerProvider.class).in(Singleton.class);
    }
}
