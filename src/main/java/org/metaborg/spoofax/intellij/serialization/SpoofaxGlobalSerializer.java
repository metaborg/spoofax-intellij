package org.metaborg.spoofax.intellij.serialization;

import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.JpsGlobal;
import org.jetbrains.jps.model.serialization.JpsGlobalExtensionSerializer;

import javax.annotation.Nullable;

public final class SpoofaxGlobalSerializer extends JpsGlobalExtensionSerializer {

    @NotNull
    public static final String NAME = "SpoofaxGlobalService";
    @NotNull public static final String CONFIG_FILE = "Spoofax.xml";

    public SpoofaxGlobalSerializer() {
        super(CONFIG_FILE, NAME);
    }

    @Override
    public final void loadExtensionWithDefaultSettings(@NotNull final JpsGlobal global) {
        loadExtensionWithState(global, null);
    }

    @Override
    public final void loadExtension(@NotNull final JpsGlobal global, @NotNull final Element element) {
        final SpoofaxGlobalState state = XmlSerializer.deserialize(element, SpoofaxGlobalState.class);
        loadExtensionWithState(global, state);
    }

    private final void loadExtensionWithState(@NotNull final JpsGlobal global, @Nullable final SpoofaxGlobalState state)
    {
        final SpoofaxGlobalConfig config = new SpoofaxGlobalConfig();
        if (state != null)
            config.loadState(state);
        SpoofaxExtensionService.getInstance().setConfiguration(global, config);
    }

    @Override
    public final void saveExtension(@NotNull final JpsGlobal jpsGlobal, @NotNull final Element element) {
        throw new UnsupportedOperationException("The `saveExtension()` method is not supported.");
    }
}
