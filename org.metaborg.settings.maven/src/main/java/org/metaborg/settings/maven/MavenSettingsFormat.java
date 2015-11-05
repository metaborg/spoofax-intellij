package org.metaborg.settings.maven;

import org.metaborg.settings.ISettingsFormat;
import org.metaborg.settings.Settings;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Settings deserializer using Maven.
 */
public class MavenSettingsFormat implements ISettingsFormat {

    @Override
    public Settings read(final InputStream input, @Nullable final Settings parent) throws IOException {
        return null;
    }

    @Override
    public void write(final OutputStream output, final Settings settings) throws IOException {

    }
}
