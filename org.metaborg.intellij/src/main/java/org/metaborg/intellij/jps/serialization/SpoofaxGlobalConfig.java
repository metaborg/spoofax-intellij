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

package org.metaborg.intellij.jps.serialization;

import com.google.inject.*;
import org.jetbrains.jps.model.*;
import org.jetbrains.jps.model.ex.*;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.configuration.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

import java.util.*;

// TODO: Rename to: JpsMetaborgApplicationConfig
/**
 * Global JPS configuration.
 */
public final class SpoofaxGlobalConfig extends SpoofaxConfig<MetaborgApplicationConfigState, SpoofaxGlobalConfig>
    implements IMetaborgApplicationConfig {

    public static final JpsElementChildRole<SpoofaxGlobalConfig> ROLE = JpsElementChildRoleBase.create("Metaborg");

    // FIXME: Make this immutable.
    private Set<LanguageIdentifier> loadedLanguages;
    private final IJpsMetaborgApplicationConfigFactory configFactory;
    @InjectLogger
    private ILogger logger;

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<LanguageIdentifier> getLoadedLanguages() {
        return this.loadedLanguages;
    }

    /**
     * Initializes a new instance of the {@link SpoofaxGlobalConfig} class.
     */
    @Inject
    public SpoofaxGlobalConfig(final IJpsMetaborgApplicationConfigFactory configFactory) {
        super(new MetaborgApplicationConfigState());

        this.configFactory = configFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final SpoofaxGlobalConfig createCopy() {
        final SpoofaxGlobalConfig config = this.configFactory.create();// new SpoofaxGlobalConfig();
        config.applyChanges(this);
        return config;
    }

    /**
     * {@inheritDoc}
     *
     * Don't use the logger here. It hasn't been injected yet.
     */
    @Override
    public void loadState(final MetaborgApplicationConfigState state) {
        super.loadState(state);
        this.loadedLanguages = new AdaptingSet<>(state.loadedLanguages,
                LanguageIdentifier::toString, LanguageIdentifier::parse);
    }
}
