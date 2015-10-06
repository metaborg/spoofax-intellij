package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;
import org.jetbrains.jps.model.serialization.module.JpsModulePropertiesSerializer;
import org.metaborg.spoofax.intellij.serialization.SpoofaxGlobalSerializer;
import org.metaborg.spoofax.intellij.serialization.SpoofaxModuleSerializer;
import org.metaborg.spoofax.intellij.serialization.SpoofaxProjectSerializer;

import java.util.Collections;
import java.util.List;

@Singleton
public final class JpsSpoofaxModelSerializerExtension extends JpsModelSerializerExtension {

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public JpsSpoofaxModelSerializerExtension() {
    }

    @NotNull
    @Override
    public final List<? extends JpsProjectExtensionSerializer> getProjectExtensionSerializers() {
        return Collections.singletonList(new SpoofaxProjectSerializer());
    }

    @NotNull
    @Override
    public final List<? extends JpsGlobalExtensionSerializer> getGlobalExtensionSerializers() {
        return Collections.singletonList(new SpoofaxGlobalSerializer());
    }

    @NotNull
    @Override
    public final List<? extends JpsModulePropertiesSerializer<?>> getModulePropertiesSerializers() {
        return Collections.singletonList(new SpoofaxModuleSerializer());
    }
}