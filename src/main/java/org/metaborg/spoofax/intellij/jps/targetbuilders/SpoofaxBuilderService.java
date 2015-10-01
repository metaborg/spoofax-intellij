package org.metaborg.spoofax.intellij.jps.targetbuilders;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.BuilderService;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.spoofax.intellij.jps.JpsPlugin;

import java.util.*;

/**
 * The Spoofax builder service.
 *
 * This service tells the JPS build system which build targetbuilders and target builders are available.
 */
public final class SpoofaxBuilderService extends BuilderService {

    private List<BuildTargetType<?>> targets;
    private List<TargetBuilder<?, ?>> builders;
    private List<ModuleLevelBuilder> moduleLevelBuilders;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxBuilderService() {
        JpsPlugin.injector().injectMembers(this);
    }

    @Inject
    private void inject(@NotNull Collection<BuildTargetType<?>> targets, @NotNull Collection<TargetBuilder<?, ?>> builders, @NotNull Collection<ModuleLevelBuilder> moduleLevelBuilders) {
        this.targets = Collections.unmodifiableList(new ArrayList<>(targets));
        this.builders = Collections.unmodifiableList(new ArrayList<>(builders));
        this.moduleLevelBuilders = Collections.unmodifiableList(new ArrayList<>(moduleLevelBuilders));
    }

    /**
     * Gets the list of build target types contributed by this plugin.
     * @return A list of build target types.
     */
    @NotNull
    @Override
    public List<? extends BuildTargetType<?>> getTargetTypes() {
        return this.targets;
    }

    @NotNull
    @Override
    public List<? extends TargetBuilder<?, ?>> createBuilders() {
        return this.builders;
    }

    @NotNull
    @Override
    public List<? extends ModuleLevelBuilder> createModuleLevelBuilders() {
        return this.moduleLevelBuilders;
    }
}
