package org.metaborg.spoofax.intellij.serialization;

import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsProject;
import org.jetbrains.jps.model.serialization.JpsProjectExtensionSerializer;

import javax.annotation.Nullable;

public final class SpoofaxProjectSerializer extends JpsProjectExtensionSerializer {

    @NotNull
    public static final String NAME = "SpoofaxProjectService";
    @NotNull
    public static final String CONFIG_FILE = "SpoofaxProject.xml";

    public SpoofaxProjectSerializer() {
        super(CONFIG_FILE, NAME);
    }

    @Override
    public final void loadExtension(@NotNull final JpsProject project, @NotNull final Element element) {
        SpoofaxProjectState state = XmlSerializer.deserialize(element, SpoofaxProjectState.class);
        loadExtensionWithState(project, state);
    }

    @Override
    public final void loadExtensionWithDefaultSettings(@NotNull final JpsProject project) {
        loadExtensionWithState(project, null);
    }

    @Override
    public final void saveExtension(@NotNull final JpsProject project, @NotNull final Element element) {
        throw new UnsupportedOperationException("The `saveExtension()` method is not supported.");
    }

    private final void loadExtensionWithState(@NotNull final JpsProject project,
                                              @Nullable final SpoofaxProjectState state) {
        final SpoofaxProjectConfig config = new SpoofaxProjectConfig();
        if (state != null)
            config.loadState(state);
        SpoofaxExtensionService.getInstance().setConfiguration(project, config);
    }

}
