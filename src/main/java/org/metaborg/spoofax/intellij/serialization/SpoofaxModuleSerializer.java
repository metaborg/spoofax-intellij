package org.metaborg.spoofax.intellij.serialization;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsDummyElement;
import org.jetbrains.jps.model.JpsElementFactory;
import org.jetbrains.jps.model.serialization.module.JpsModulePropertiesSerializer;
import org.metaborg.spoofax.intellij.JpsSpoofaxModuleType;

import javax.annotation.Nullable;

public final class SpoofaxModuleSerializer extends JpsModulePropertiesSerializer<JpsDummyElement> {

    public SpoofaxModuleSerializer() {
        super(JpsSpoofaxModuleType.INSTANCE, "SPOOFAX_MODULE", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public final JpsDummyElement loadProperties(@Nullable final Element element) {
        JpsDummyElement result = JpsElementFactory.getInstance().createDummyElement();

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void saveProperties(@NotNull final JpsDummyElement jpsDummyElement, @NotNull final Element element) {
        throw new UnsupportedOperationException("The `saveProperties()` method is not supported.");
    }

}
