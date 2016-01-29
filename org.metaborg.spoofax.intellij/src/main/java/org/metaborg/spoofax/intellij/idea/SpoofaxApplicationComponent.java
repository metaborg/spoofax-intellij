/*
 * Copyright © 2015-2016
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

package org.metaborg.spoofax.intellij.idea;

import com.google.inject.Inject;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.logging.InjectLogger;
import org.metaborg.spoofax.intellij.languages.LanguageManager;
import org.slf4j.Logger;

public class SpoofaxApplicationComponent implements ApplicationComponent {

    @InjectLogger
    private Logger logger;
    private LanguageManager languageManager;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxApplicationComponent() {
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(@NotNull final LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    /**
     * Initializes the plugin.
     */
    @Override
    public final void initComponent() {
        this.languageManager.loadMetaLanguages();
    }

    /**
     * Disposes the plugin.
     */
    @Override
    public final void disposeComponent() {

    }

    /**
     * Gets the name of the application component.
     *
     * @return The name of the component.
     */
    @Override
    @NotNull
    public String getComponentName() {
        return SpoofaxApplicationComponent.class.getName();
    }
}
