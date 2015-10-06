package org.metaborg.spoofax.intellij;

import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.ex.JpsElementTypeWithDummyProperties;
import org.jetbrains.jps.model.module.JpsModuleType;

/**
 * A JPS Spoofax module type, used to identify Spoofax JPS modules.
 */
@Singleton
public final class JpsSpoofaxModuleType extends JpsElementTypeWithDummyProperties implements JpsModuleType<JpsDummyElement> {

    @NotNull
    public static final JpsSpoofaxModuleType INSTANCE = new JpsSpoofaxModuleType();

    private JpsSpoofaxModuleType() {
    }

}