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

import com.google.inject.*;
import org.jetbrains.jps.incremental.*;
import org.metaborg.intellij.jps.builders.*;
import org.metaborg.spoofax.meta.core.SpoofaxMetaModule;

import java.util.*;

/**
 * The Guice dependency injection module for the Spoofax JPS meta plugin.
 */
/* package private */ class JpsSpoofaxMetaModule extends SpoofaxMetaModule {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        super.configure();

        bindBuilders();
    }

    /**
     * Binds the builders.
     */
    protected void bindBuilders() {
        bind(SpoofaxPreBuilder.class).in(Singleton.class);
        bind(SpoofaxPostBuilder.class).in(Singleton.class);
    }

    @SuppressWarnings("unused")
    @Singleton
    @Provides
    @Inject
    public final Collection<TargetBuilder<?, ?>> provideTargetBuilders(
            final SpoofaxPreBuilder preBuilder,
            final SpoofaxPostBuilder postBuilder) {
        return Arrays.asList(preBuilder, postBuilder);
    }
}
