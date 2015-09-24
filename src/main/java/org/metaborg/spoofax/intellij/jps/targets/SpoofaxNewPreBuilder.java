package org.metaborg.spoofax.intellij.jps.targets;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Builds the Spoofax build target.
 */
@Singleton
public final class SpoofaxNewPreBuilder extends SpoofaxBuilder<SpoofaxNewPreTarget> {


    @Inject
    private SpoofaxNewPreBuilder(SpoofaxNewPreTargetType targetType) {
        super(targetType);
    }


}