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

import com.google.common.collect.Lists;
import org.junit.Test;
import org.metaborg.settings.jackson.YamlSettingsFormat;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class YamlSettingsFormatTests {

    @Test
    public void readWriteStubSettings() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());
        sut.addSerializerDeserializer(new ComplexObjectSerializer(), new ComplexObjectDeserializer());

        String input = "---\n"
                + "name: \"testName\"\n"
                + "obj:\n"
                + "  name: \"test\"\n"
                + "  value: 1\n"
                + "listOfObjs:\n"
                + "- name: \"testname\"\n"
                + "  value: 4\n"
                + "- name: \"other\"\n"
                + "  value: 20\n";
        SettingsStub settings = (SettingsStub) SettingsFormatUtils.readFromString(sut, input);

        assertEquals("testName", settings.name());
        assertEquals(new ComplexObject("test", 1), settings.obj());
        assertEquals(Arrays.asList(
                new ComplexObject("testname", 4),
                new ComplexObject("other", 20)
        ), settings.listOfObjs());

        String output = SettingsFormatUtils.writeToString(sut, settings);

        assertEquals(input, output);
    }

    @Test
    public void writeStubSettings() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());
        sut.addSerializerDeserializer(new ComplexObjectSerializer(), new ComplexObjectDeserializer());

        MutableSettings settings = new MutableSettings();
        settings.setLocalSetting(SettingsStub.NAME_KEY, "testName");
        settings.setLocalSetting(SettingsStub.OBJ_KEY, new ComplexObject("test", 1));
        settings.setLocalSetting(SettingsStub.LIST_OF_OBJS_KEY, Lists.newArrayList(
                new ComplexObject("testname", 4),
                new ComplexObject("other", 20)
        ));

        String output = SettingsFormatUtils.writeToString(sut, settings);
        String expected = "---\n"
                + "name: \"testName\"\n"
                + "obj:\n"
                + "  name: \"test\"\n"
                + "  value: 1\n"
                + "listOfObjs:\n"
                + "- name: \"testname\"\n"
                + "  value: 4\n"
                + "- name: \"other\"\n"
                + "  value: 20\n";

        assertEquals(expected, output);
    }

    @Test
    public void settingsHaveParent() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());
        Settings parent = new Settings(new HashMap<>(), null);
        String input = "---\n"
                + "name: \"testName\"\n";
        SettingsStub settings = (SettingsStub) SettingsFormatUtils.readFromString(sut, input, parent);

        assertSame(parent, settings.parent());
    }

    @Test
    public void unknownSettingsAreConserved() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());
        Settings parent = new Settings(new HashMap<>(), null);
        String input = "---\n"
                + "name: \"testName\"\n"
                + "dummy: \"some Value\"\n";
        String output = SettingsFormatUtils.writeToString(sut, SettingsFormatUtils.readFromString(sut, input, parent));

        assertEquals(input, output);
    }

    @Test
    public void childOverridesParent() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());
        String parentInput = "---\n"
                + "name: \"parent\"\n";
        SettingsStub parent = (SettingsStub) SettingsFormatUtils.readFromString(sut, parentInput);
        String childInput = "---\n"
                + "name: \"child\"\n";
        SettingsStub child = (SettingsStub) SettingsFormatUtils.readFromString(sut, childInput, parent);

        assertEquals("child", child.name());
        assertEquals("child", child.getLocalSetting(SettingsStub.NAME_KEY));
    }

    @Test
    public void childDoesNotOverrideParent() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());
        String parentInput = "---\n"
                + "name: \"parent\"\n";
        SettingsStub parent = (SettingsStub) SettingsFormatUtils.readFromString(sut, parentInput);
        String childInput = "---\n"
                + "id: \"unrelated\"\n";
        SettingsStub child = (SettingsStub) SettingsFormatUtils.readFromString(sut, childInput, parent);

        assertEquals("parent", child.name());
        assertEquals(null, child.getLocalSettingOrDefault(SettingsStub.NAME_KEY, null));
    }

    @Test
    public void childAppendsParent() throws IOException {
        YamlSettingsFormat sut = new YamlSettingsFormat(new SettingsStubFactory());
        sut.addSerializerDeserializer(new ComplexObjectSerializer(), new ComplexObjectDeserializer());
        String parentInput = "---\n"
                + "listOfObjs:\n"
                + "- name: \"testname\"\n"
                + "  value: 4\n";
        SettingsStub parent = (SettingsStub) SettingsFormatUtils.readFromString(sut, parentInput);
        String childInput = "---\n"
                + "listOfObjs:\n"
                + "- name: \"other\"\n"
                + "  value: 20\n";
        SettingsStub child = (SettingsStub) SettingsFormatUtils.readFromString(sut, childInput, parent);

        assertEquals(Arrays.asList(
                new ComplexObject("testname", 4),
                new ComplexObject("other", 20)
        ), child.listOfObjs());
    }

}
