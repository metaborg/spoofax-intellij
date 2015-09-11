package org.metaborg.spoofax.intellij.serialization;

import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;

public class SpoofaxProjectSerializer extends JpsProjectExtensionSerializer {

    public static final String NAME = "SpoofaxProjectService";
    public static final String CONFIG_FILE = "SpoofaxProject.xml";

    public SpoofaxProjectSerializer() {
        super(CONFIG_FILE, NAME);
    }

    @Override
    public void loadExtensionWithDefaultSettings(JpsProject project) {
        loadExtensionWithState(project, null);
    }

    @Override
    public void loadExtension(JpsProject project, Element element) {
        SpoofaxProjectState state = XmlSerializer.deserialize(element, SpoofaxProjectState.class);
        loadExtensionWithState(project, state);
    }

    private void loadExtensionWithState(JpsProject project, SpoofaxProjectState state)
    {
        final SpoofaxProjectConfig config = new SpoofaxProjectConfig();
        if (state != null)
            config.loadState(state);
        SpoofaxExtensionService.getInstance().setConfiguration(project, config);
    }

    @Override
    public void saveExtension(JpsProject project, Element element) {
        throw new UnsupportedOperationException("The `saveExtension()` method is not supported.");
    }

}
