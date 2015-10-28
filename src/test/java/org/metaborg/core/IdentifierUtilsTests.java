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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public final class IdentifierUtilsTests {


    @Test
    public void createReturnsNumber() {
        String result = IdentifierUtils.create();
        Integer.parseInt(result);
    }

    @Test
    public void createReturnsDifferentIdentifierEachTime() {
        String result1 = IdentifierUtils.create();
        String result2 = IdentifierUtils.create();

        assertNotEquals(result1, result2);
    }

    @Test
    public void createWithPrefixReturnsPrefixedNumber() {
        String result = IdentifierUtils.create("x_");
        assertTrue(result.startsWith("x_"));
        Integer.parseInt(result.substring(2));
    }

    @Test
    public void createWithPrefixReturnsDifferentIdentifierEachTime() {
        String result1 = IdentifierUtils.create("x_");
        String result2 = IdentifierUtils.create("x_");

        assertNotEquals(result1, result2);
    }
}
