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

package org.metaborg.intellij.jps;

import com.google.inject.matcher.Matchers;
import org.metaborg.intellij.logging.MetaborgLoggerTypeListener;
import org.metaborg.intellij.logging.Slf4JLoggerTypeListener;
import org.metaborg.spoofax.core.SpoofaxModule;

/**
 * The Guice dependency injection module for the Spoofax JPS plugin.
 */
/* package private */ final class JpsSpoofaxModule extends SpoofaxModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();
        bindLoggerListeners();
    }

    /**
     * Binds listeners for injected loggers.
     */
    protected void bindLoggerListeners() {
        // FIXME: Are these the loggers we want to use for JPS plugins?
        bindListener(Matchers.any(), new Slf4JLoggerTypeListener());
        bindListener(Matchers.any(), new MetaborgLoggerTypeListener());
    }
}
