package org.metaborg.spoofax.intellij;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.builders.BuildTargetLoader;
import org.jetbrains.jps.builders.ModuleBasedBuildTargetType;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsModel;
import org.jetbrains.jps.model.module.JpsTypedModule;

import java.util.ArrayList;
import java.util.List;

/**
 * The Spoofax production build target type.
 */
@Singleton
public final class SpoofaxProductionTargetType extends SpoofaxTargetType {

    public static final String TARGET_TYPE_ID = "spoofax_production";

    @Inject
    private SpoofaxProductionTargetType(JpsSpoofaxModuleType moduleType) {
        super(TARGET_TYPE_ID, BuildTargetKind.PRODUCTION, moduleType);
    }
}
