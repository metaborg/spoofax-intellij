package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.metaborg.spoofax.intellij.SpoofaxPostTarget;
import org.metaborg.spoofax.intellij.SpoofaxPostTargetType;
import org.metaborg.spoofax.intellij.SpoofaxPreTarget;
import org.metaborg.spoofax.intellij.SpoofaxPreTargetType;

/**
 * Builds the Spoofax build target.
 */
@Singleton
public final class SpoofaxPostBuilder extends SpoofaxBuilder<SpoofaxPostTarget> {


    @Inject
    private SpoofaxPostBuilder(SpoofaxPostTargetType targetType) {
        super(targetType);
    }


}