package org.metaborg.spoofax.intellij.jps.targetbuilders;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.BuilderService;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.spoofax.intellij.jps.JpsPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The Spoofax builder service.
 * <p>
 * This service tells the JPS build system which build targetbuilders and target builders are available.
 */
@Singleton
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
    private void inject(@NotNull Collection<BuildTargetType<?>> targets,
                        @NotNull Collection<TargetBuilder<?, ?>> builders,
                        @NotNull Collection<ModuleLevelBuilder> moduleLevelBuilders) {
        this.targets = Collections.unmodifiableList(new ArrayList<>(targets));
        this.builders = Collections.unmodifiableList(new ArrayList<>(builders));
        this.moduleLevelBuilders = Collections.unmodifiableList(new ArrayList<>(moduleLevelBuilders));
    }

    /**
     * Gets the list of build target types contributed by this plugin.
     *
     * @return A list of build target types.
     */
    @NotNull
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
    @NotNull
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
    @NotNull
    @Override
    public List<? extends TargetBuilder<?, ?>> createBuilders() {
        return this.builders;
    }
}
