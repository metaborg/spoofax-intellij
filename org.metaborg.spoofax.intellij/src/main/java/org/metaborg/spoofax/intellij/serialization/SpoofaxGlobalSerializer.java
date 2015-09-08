package org.metaborg.spoofax.intellij.serialization;

import com.intellij.openapi.util.JDOMExternalizerUtil;
import org.jdom.Element;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;

import javax.annotation.Nullable;

public class SpoofaxGlobalSerializer extends JpsGlobalExtensionSerializer {
    private static final String MY_NAME_NAME = "MY_NAME";

    public SpoofaxGlobalSerializer() {
        super("Spoofax.xml", "SpoofaxGlobalConfig");
    }

    @Override
    public void loadExtensionWithDefaultSettings(JpsGlobal global) {
        loadExtensionFromJDom(global, null);
    }

    @Override
    public void loadExtension(JpsGlobal global, Element componentTag) {
        loadExtensionFromJDom(global, componentTag);
    }

    private void loadExtensionFromJDom(JpsGlobal global, @Nullable Element componentTag) {
        final SpoofaxGlobalConfigImpl configuration = new SpoofaxGlobalConfigImpl();

        if (componentTag != null) {
            final String myName = JDOMExternalizerUtil.readField(componentTag, MY_NAME_NAME);
            if (myName != null) {
                configuration.setMyName(myName);
            }
        }

        SpoofaxExtensionService.getInstance().setConfiguration(global, configuration);
    }

    @Override
    public void saveExtension(JpsGlobal jpsGlobal, Element componentTag) {

    }
}
