package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.BuilderService;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;
import org.metaborg.spoofax.intellij.jps.builders.SpoofaxSourceGenBuilder;

import java.util.*;

/**
 * The Spoofax builder service.
 *
 * This service tells the JPS build system which build targets and target builders are available.
 */
public final class SpoofaxBuilderService extends BuilderService {

    private List<BuildTargetType<?>> targets;
    private List<TargetBuilder<?, ?>> builders;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxBuilderService() {
        JpsPlugin.injector().injectMembers(this);
    }

    @Inject
    private void inject(Collection<BuildTargetType<?>> targets, Collection<TargetBuilder<?, ?>> builders) {
        this.targets = Collections.unmodifiableList(new ArrayList<>(targets));
        this.builders = Collections.unmodifiableList(new ArrayList<>(builders));
    }

    /**
     * Gets the list of build target types contributed by this plugin.
     * @return A list of build target types.
     */
    @NotNull
    @Override
    public List<? extends BuildTargetType<?>> getTargetTypes() {
        return this.targets;
        //return Arrays.asList(SpoofaxNewPreTargetType.PRODUCTION, SpoofaxPostTargetType.PRODUCTION);
    }

    @NotNull
    @Override
    public List<? extends TargetBuilder<?, ?>> createBuilders() {
        return this.builders;
        //return Arrays.asList(SpoofaxOldBuilder.INSTANCE, SpoofaxBuilder.INSTANCE);
    }

    @NotNull
    @Override
    public List<? extends ModuleLevelBuilder> createModuleLevelBuilders() {
        return Arrays.asList(new SpoofaxSourceGenBuilder());
    }
}
