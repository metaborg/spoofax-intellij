/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.jps.configuration;

import com.google.inject.*;
import org.jetbrains.jps.model.JpsElementChildRole;
import org.jetbrains.jps.model.ex.JpsElementChildRoleBase;
import org.metaborg.core.language.*;
import org.metaborg.intellij.configuration.AdaptingSet;
import org.metaborg.intellij.configuration.IMetaborgApplicationConfig;
import org.metaborg.intellij.configuration.MetaborgApplicationConfigState;
import org.metaborg.intellij.logging.InjectLogger;
import org.metaborg.util.log.*;

import java.util.Set;

/**
 * Application-wide JPS configuration.
 */
public final class JpsMetaborgApplicationConfig
        extends AbstractMetaborgConfig<MetaborgApplicationConfigState, JpsMetaborgApplicationConfig>
        implements IMetaborgApplicationConfig {

    public static final JpsElementChildRole<JpsMetaborgApplicationConfig> ROLE
            = JpsElementChildRoleBase.create("Metaborg");

    // Don't initialize fields that depend on the state here. Initialize in loadState().
    // FIXME: Make this an immutable set.
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
     * Initializes a new instance of the {@link JpsMetaborgApplicationConfig} class.
     */
    @Inject
    public JpsMetaborgApplicationConfig(final IJpsMetaborgApplicationConfigFactory configFactory) {
        super(new MetaborgApplicationConfigState());
        // Don't initialize fields that depend on the state here. Initialize in loadState().
        this.configFactory = configFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final JpsMetaborgApplicationConfig createCopy() {
        final JpsMetaborgApplicationConfig config = this.configFactory.create();// new SpoofaxGlobalConfig();
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
        // Initialize fields that depend on state here.
        this.loadedLanguages = new AdaptingSet<>(state.loadedLanguages,
                LanguageIdentifier::toString, LanguageIdentifier::parse);
    }
}
