package org.metaborg.spoofax.intellij.serialization;

import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;

import javax.annotation.Nullable;

public class SpoofaxGlobalSerializer extends JpsGlobalExtensionSerializer {

    public SpoofaxGlobalSerializer() {
        super("Spoofax.xml", "SpoofaxGlobalComponent");
    }

    @Override
    public void loadExtensionWithDefaultSettings(JpsGlobal global) {
        loadExtensionWithState(global, null);
    }

    @Override
    public void loadExtension(JpsGlobal global, Element element) {
        SpoofaxGlobalState state = XmlSerializer.deserialize(element, SpoofaxGlobalState.class);
        loadExtensionWithState(global, state);
    }

    private void loadExtensionWithState(JpsGlobal global, SpoofaxGlobalState state)
    {
        final SpoofaxGlobalConfigImpl config = new SpoofaxGlobalConfigImpl();
        if (state != null)
            config.loadState(state);
        SpoofaxExtensionService.getInstance().setConfiguration(global, config);
    }

    @Override
    public void saveExtension(JpsGlobal jpsGlobal, Element element) {
        throw new UnsupportedOperationException("The `saveExtension()` method is not supported.");
    }
}
