package org.metaborg.spoofax.intellij.jps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsElementFactory;
import org.jetbrains.jps.model.ex.JpsElementBase;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;
import org.jetbrains.jps.model.serialization.library.JpsLibraryTableSerializer;
import org.jetbrains.jps.model.serialization.module.JpsModulePropertiesSerializer;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;
import org.metaborg.spoofax.intellij.serialization.SpoofaxGlobalSerializer;
import org.metaborg.spoofax.intellij.serialization.SpoofaxModuleSerializer;
import org.metaborg.spoofax.intellij.serialization.SpoofaxProjectSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Singleton
public class JpsSpoofaxModelSerializerExtension extends JpsModelSerializerExtension {

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public JpsSpoofaxModelSerializerExtension() {
    }


    @NotNull
    @Override
    public List<? extends JpsGlobalExtensionSerializer> getGlobalExtensionSerializers() {
        return Collections.singletonList(new SpoofaxGlobalSerializer());
    }

    @NotNull
    @Override
    public List<? extends JpsProjectExtensionSerializer> getProjectExtensionSerializers() {
        //Arrays.asList(new SpoofaxProjectSerializer());
        return Collections.singletonList(new SpoofaxProjectSerializer());
    }

    @NotNull
    @Override
    public List<? extends JpsModulePropertiesSerializer<?>> getModulePropertiesSerializers() {
        return Collections.singletonList(new SpoofaxModuleSerializer());
        /*
        return Collections.singletonList(new JpsModulePropertiesSerializer<JpsDummyElement>(JpsSpoofaxModuleType.INSTANCE, "SPOOFAX_MODULE", null) {
            @Override
            public JpsDummyElement loadProperties(@Nullable Element componentElement) {
                return JpsElementFactory.getInstance().createDummyElement();
            }

            @Override
            public void saveProperties(@NotNull JpsDummyElement properties, @NotNull Element componentElement) {
            }
        });*/
    }
}