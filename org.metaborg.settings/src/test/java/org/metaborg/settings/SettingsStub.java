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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Settings stub.
 */
public final class SettingsStub extends Settings {

    /* package private */ static final SettingKey<String> NAME_KEY
            = new SettingKey<>("name", String.class);
    /* package private */ static final SettingKey<ComplexObject> OBJ_KEY
            = new SettingKey<>("obj", ComplexObject.class);
    /* package private */ static final SettingKey<List<ComplexObject>> LIST_OF_OBJS_KEY
            = new SettingKey<>("listOfObjs", new TypeReference<List<ComplexObject>>(){}, SettingStrategies.appendList());

    public SettingsStub(final Map<ISettingKey<?>, Object> settings, @Nullable final Settings parent) {
        super(settings, parent);
    }

    public String name() {
        return getSetting(NAME_KEY);
    }

    public ComplexObject obj() { return getSetting(OBJ_KEY); }

    public List<ComplexObject> listOfObjs() { return getSetting(LIST_OF_OBJS_KEY); }

}

