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

package org.metaborg.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class StringFormatterTests {

    @Test
    public void formatsStringWithoutPlaceholders() {
        String input = "String without placeholders.";
        String result = StringFormatter.format(input);

        assertEquals(input, result);
    }

    @Test
    public void formatsStringWithOnePlaceholder() {
        String input = "String {} placeholders.";
        String result = StringFormatter.format(input, "with");

        assertEquals("String with placeholders.", result);
    }

    @Test
    public void formatsStringWithTwoPlaceholders() {
        String input = "String {} {} placeholders.";
        String result = StringFormatter.format(input, "with", "some");

        assertEquals("String with some placeholders.", result);
    }

    @Test
    public void formatsStringWithThreePlaceholders() {
        String input = "String {} {} {} placeholders.";
        String result = StringFormatter.format(input, "with", "some", "more");

        assertEquals("String with some more placeholders.", result);
    }

    @Test
    public void formatsStringWithFourPlaceholders() {
        String input = "String {} {} {} {} placeholders.";
        String result = StringFormatter.format(input, "with", "an", "array", "of");

        assertEquals("String with an array of placeholders.", result);
    }

    @Test
    public void formatsStringWithValueArray() {
        String input = "String {} {} {} {} placeholders.";
        String result = StringFormatter.format(input, new Object[]{"with", "an", "array", "of"});

        assertEquals("String with an array of placeholders.", result);
    }
}
