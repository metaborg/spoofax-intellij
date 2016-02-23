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

import com.google.inject.*;
import org.jetbrains.jps.builders.*;
import org.jetbrains.jps.incremental.*;
import org.metaborg.intellij.jps.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import java.util.*;

/**
 * The Spoofax builder service.
 * <p>
 * This service tells the JPS build system which (module-level) target builders are available.
 */
@Singleton
public final class SpoofaxBuilderService extends BuilderService {

    private List<BuildTargetType<?>> targets;
    private List<TargetBuilder<?, ?>> builders;
    private List<ModuleLevelBuilder> moduleLevelBuilders;
    @InjectLogger
    private ILogger logger;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public SpoofaxBuilderService() {
        super();
        SpoofaxJpsPlugin.injector().injectMembers(this);

        this.logger.debug("Builder service constructed! Classloader: {}",
                this.getClass().getClassLoader().getClass().getName());
    }

    @SuppressWarnings("unused")
    @Inject
    private void inject(
            final Collection<BuildTargetType<?>> targets,
            final Collection<TargetBuilder<?, ?>> builders,
            final Collection<ModuleLevelBuilder> moduleLevelBuilders) {
        this.targets = Collections.unmodifiableList(new ArrayList<>(targets));
        this.builders = Collections.unmodifiableList(new ArrayList<>(builders));
        this.moduleLevelBuilders = Collections.unmodifiableList(new ArrayList<>(moduleLevelBuilders));
    }

    /**
     * Gets the list of build target types contributed by this plugin.
     *
     * @return A list of build target types.
     */
    @Override
    public List<? extends BuildTargetType<?>> getTargetTypes() {
        return this.targets;
    }

    /**
     * Gets the list of module level builders contributed by this plugin.
     * <p>
     * A module level builder is a phase in the compilation of the Java build target.
     *
     * @return A list of module level builders.
     */
    @Override
    public List<? extends ModuleLevelBuilder> createModuleLevelBuilders() {
        return this.moduleLevelBuilders;
    }

    /**
     * Gets the list of target builders contributed by this plugin.
     * <p>
     * A target builder compiles any build target.
     *
     * @return A list of target builders.
     */
    @Override
    public List<? extends TargetBuilder<?, ?>> createBuilders() {
        return this.builders;
    }
}
