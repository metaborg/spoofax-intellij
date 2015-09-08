package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsElementFactory;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.library.JpsLibraryTableSerializer;
import org.jetbrains.jps.model.serialization.module.JpsModulePropertiesSerializer;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;
import org.metaborg.spoofax.intellij.serialization.SpoofaxGlobalSerializer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JpsSpoofaxModelSerializerExtension extends JpsModelSerializerExtension {

    private JpsSpoofaxModuleType moduleType;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public JpsSpoofaxModelSerializerExtension() {
        JpsPlugin.injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject(JpsSpoofaxModuleType moduleType) {
        this.moduleType = moduleType;
    }

    @NotNull
    @Override
    public List<? extends JpsGlobalExtensionSerializer> getGlobalExtensionSerializers() {
        return Collections.singletonList(new SpoofaxGlobalSerializer());
    }

    @NotNull
    @Override
    public List<? extends JpsModulePropertiesSerializer<?>> getModulePropertiesSerializers() {
        return Collections.singletonList(new JpsModulePropertiesSerializer<JpsDummyElement>(this.moduleType, "SPOOFAX_MODULE", null) {
            @Override
            public JpsDummyElement loadProperties(@Nullable Element componentElement) {
                return JpsElementFactory.getInstance().createDummyElement();
            }

            @Override
            public void saveProperties(@NotNull JpsDummyElement properties, @NotNull Element componentElement) {
            }
        });
    }
}