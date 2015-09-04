package org.metaborg.spoofax.intellij;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.ex.JpsElementTypeWithDummyProperties;
import org.jetbrains.jps.model.module.JpsModuleType;

/**
 * A JPS Spoofax module type, used to identify Spoofax JPS modules.
 */
@Singleton
public class JpsSpoofaxModuleType extends JpsElementTypeWithDummyProperties implements JpsModuleType<JpsDummyElement> {

    @Inject
    private JpsSpoofaxModuleType() { }

}