package org.metaborg.spoofax.intellij.jps.targets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.metaborg.spoofax.intellij.SpoofaxPostTarget;
import org.metaborg.spoofax.intellij.SpoofaxPostTargetType;
import org.metaborg.spoofax.intellij.jps.targets.SpoofaxBuilder;

/**
 * Builds the Spoofax build target.
 */
@Singleton
public final class SpoofaxNewPostBuilder extends SpoofaxBuilder<SpoofaxNewPostTarget> {


    @Inject
    private SpoofaxNewPostBuilder(SpoofaxNewPostTargetType targetType) {
        super(targetType);
    }


}