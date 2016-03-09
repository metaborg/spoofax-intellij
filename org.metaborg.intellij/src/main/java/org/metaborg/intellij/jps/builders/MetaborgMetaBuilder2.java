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

package org.metaborg.intellij.jps.builders;

import com.virtlink.tartarus.*;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.incremental.*;
import org.metaborg.core.project.*;
import org.metaborg.intellij.jps.projects.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.intellij.utils.*;
import org.metaborg.spoofax.meta.core.build.*;
import org.metaborg.spoofax.meta.core.project.*;
import org.metaborg.util.log.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * Metaborg meta-builder.
 */
public abstract class MetaborgMetaBuilder2<T extends SpoofaxTarget> extends TargetBuilder<SpoofaxSourceRootDescriptor, T> {

    protected final IJpsProjectService projectService;
    protected final ISpoofaxLanguageSpecService languageSpecService;
    protected final JpsSpoofaxMetaBuilder jpsSpoofaxMetaBuilder;
    @InjectLogger
    private ILogger logger;

    /**
     * Gets the presentable name of the builder.
     *
     * @return The name.
     */
    @Override
    public abstract String getPresentableName();

    /**
     * Initializes a new instance of the {@link MetaborgMetaBuilder2} class.
     *
     * @param targetType The target type.
     */
    protected MetaborgMetaBuilder2(
            final BuildTargetType<T> targetType,
            final JpsSpoofaxMetaBuilder jpsSpoofaxMetaBuilder,
            final IJpsProjectService projectService,
            final ISpoofaxLanguageSpecService languageSpecService) {
        super(Collections.singletonList(targetType));
        this.jpsSpoofaxMetaBuilder = jpsSpoofaxMetaBuilder;
        this.projectService = projectService;
        this.languageSpecService = languageSpecService;
    }

    /**
     * Builds the build target.
     *
     * @param target   The build target.
     * @param holder   The dirty files holder.
     * @param consumer The build output consumer.
     * @param context  The compile context.
     * @throws ProjectBuildException
     * @throws IOException
     */
    @Override
    public final void build(
            final T target,
            final DirtyFilesHolder<SpoofaxSourceRootDescriptor, T> holder,
            final BuildOutputConsumer consumer,
            final CompileContext context) throws ProjectBuildException, IOException {

        try {
            final LanguageSpecBuildInput metaInput = this.jpsSpoofaxMetaBuilder
                    .getLanguageSpecBuildInput(target.getModule());

            doBuild(metaInput, target, holder, consumer, context);

        } catch (final ProjectBuildException e) {
            this.logger.error("An unexpected project build exception occurred.", e);
            throw e;
        } catch (final ProjectException e) {
            this.logger.error("An unexpected project exception occurred.", e);
            throw new ProjectBuildException(e);
        } catch (final Exception e) {
            this.logger.error("An unexpected exception occurred.", e);
            throw new ProjectBuildException(e);
        }
    }

    /**
     * Executes the build.
     *
     * @throws ProjectBuildException
     * @throws IOException
     */
    public abstract void doBuild(
            final LanguageSpecBuildInput metaInput,
            final T target,
            final DirtyFilesHolder<SpoofaxSourceRootDescriptor, T> holder,
            final BuildOutputConsumer consumer,
            final CompileContext context) throws Exception;




}