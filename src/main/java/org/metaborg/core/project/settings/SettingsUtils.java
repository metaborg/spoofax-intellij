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

package org.metaborg.core.project.settings;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

/**
 * Created by daniel on 10/28/15.
 */
/* package private */ final class SettingsUtils {

    /**
     * Asserts that there are no cycles in the dependency chain.
     *
     * @param settings The settings to test.
     */
    public static void assertNoCycles(@NotNull final Settings settings) {
        Preconditions.checkNotNull(settings);

        Settings current = settings.parent();
        while (current != null && current != settings) {
            current = current.parent();
        }
        if (current == settings)
            throw new RuntimeException("There is a cycle in the settings dependency chain.");
        assert current == null;
    }

}
