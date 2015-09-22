package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.BuilderService;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.spoofax.intellij.SpoofaxPreTargetType;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;
import org.metaborg.spoofax.intellij.jps.builders.SpoofaxRegularBuilder;
import org.metaborg.spoofax.intellij.jps.builders.SpoofaxSourceGenBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The Spoofax builder service.
 *
 * This service tells the JPS build system which build targets and target builders are available.
 */
public final class SpoofaxBuilderService extends BuilderService {



    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxBuilderService() {

    }

    /**
     * Gets the list of build target types contributed by this plugin.
     * @return A list of build target types.
     */
    @NotNull
    @Override
    public List<? extends BuildTargetType<?>> getTargetTypes() {
        return Arrays.asList(SpoofaxPreTargetType.PRODUCTION, SpoofaxTargetType.PRODUCTION);
    }

    @NotNull
    @Override
    public List<? extends TargetBuilder<?, ?>> createBuilders() {
        return Arrays.asList(SpoofaxBuilder.INSTANCE, SpoofaxPreBuilder.INSTANCE);
    }

    @NotNull
    @Override
    public List<? extends ModuleLevelBuilder> createModuleLevelBuilders() {
        return Arrays.asList(new SpoofaxSourceGenBuilder());
    }
}
