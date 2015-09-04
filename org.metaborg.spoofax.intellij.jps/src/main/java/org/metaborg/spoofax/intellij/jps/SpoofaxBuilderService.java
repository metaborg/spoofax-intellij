package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildTargetType;
import org.jetbrains.jps.incremental.BuilderService;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;

import java.util.Collections;
import java.util.List;

/**
 * The Spoofax builder service.
 *
 * This service tells the JPS build system which build targets and target builders are available.
 */
public final class SpoofaxBuilderService extends BuilderService {

    private SpoofaxTargetType targetType;
    private SpoofaxTargetBuilder targetBuilder;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxBuilderService() {
        JpsPlugin.injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(SpoofaxTargetType targetType, SpoofaxTargetBuilder targetBuilder) {
        this.targetType = targetType;
        this.targetBuilder = targetBuilder;
    }

    /**
     * Gets the list of build target types contributed by this plugin.
     * @return A list of build target types.
     */
    @NotNull
    @Override
    public List<? extends BuildTargetType<?>> getTargetTypes() {
        return Collections.singletonList(this.targetType);
    }

    /**
     * Gets a list of non-module target builders contributed by this plugin.
     * @return A list of target builders.
     */
    @NotNull
    @Override
    public List<? extends TargetBuilder<?,?>> createBuilders() {
        return Collections.singletonList(this.targetBuilder);
    }
}
