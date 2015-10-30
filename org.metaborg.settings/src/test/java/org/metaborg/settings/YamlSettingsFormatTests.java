/*
 * Copyright © 2015-2015
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.settings;

import org.junit.Test;
import org.metaborg.core.language.LanguageIdentifier;
import org.metaborg.settings.DefaultSettings;
import org.metaborg.settings.Settings;
import org.metaborg.core.project.settings.SettingsStub;
import org.metaborg.core.project.settings.SettingsStubFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;

public final class YamlSettingsFormatTests {

    @Test
    public void readWriteStubSettings() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());

        String input = "---\n"
                + "name: \"testName\"\n"
                + "id: \"org.test:testclass:1.5.0\"\n"
                + "listOfIds:\n"
                + "- \"org.test:dep1:1.0.0\"\n"
                + "- \"org.test:dep2:0.1.0\"\n";
        SettingsStub settings = (SettingsStub) sut.readFromString(input);

        assertEquals("testName", settings.name());
        assertEquals(LanguageIdentifier.parse("org.test:testclass:1.5.0"), settings.id());
        assertEquals(Arrays.asList(
                LanguageIdentifier.parse("org.test:dep1:1.0.0"),
                LanguageIdentifier.parse("org.test:dep2:0.1.0")
        ), settings.listOfIds());

        String output = sut.writeToString(settings);

        assertEquals(input, output);
    }

    @Test
    public void settingsHaveParent() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());
        Settings parent = new DefaultSettings(new HashMap<>());
        String input = "---\n"
                + "name: \"testName\"\n";
        SettingsStub settings = (SettingsStub) sut.readFromString(input, parent);

        assertSame(parent, settings.parent());
    }

    @Test
    public void unknownSettingsAreConserved() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());
        Settings parent = new DefaultSettings(new HashMap<>());
        String input = "---\n"
                + "name: \"testName\"\n"
                + "dummy: \"some Value\"\n";
        String output = sut.writeToString(sut.readFromString(input, parent));

        assertEquals(input, output);
    }

}
