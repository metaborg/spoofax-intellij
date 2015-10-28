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

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

public final class CollectionUtilsTests {

    @Test
    public void toListEmptyIterable() {
        Iterable<String> iterable = Collections.emptyList();

        List<String> result = CollectionUtils.toList(iterable);

        assertEquals(iterable, result);
        assertNotSame(iterable, result);
    }

    @Test
    public void toListNonEmptyIterable() {
        Iterable<String> iterable = Arrays.asList("a", "b", "c");

        List<String> result = CollectionUtils.toList(iterable);

        assertEquals(iterable, result);
        assertNotSame(iterable, result);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void toListIsUnmodifiable() {
        List<String> result = CollectionUtils.toList(Arrays.asList("a", "b", "c"));
        result.add("x");
    }

    @Test
    public void toMutableListEmptyIterable() {
        Iterable<String> iterable = Collections.emptyList();

        List<String> result = CollectionUtils.toMutableList(iterable);

        assertEquals(iterable, result);
        assertNotSame(iterable, result);
    }

    @Test
    public void toMutableListNonEmptyIterable() {
        Iterable<String> iterable = Arrays.asList("a", "b", "c");

        List<String> result = CollectionUtils.toMutableList(iterable);

        assertEquals(iterable, result);
        assertNotSame(iterable, result);
    }

    @Test
    public void toMutableListIsModifiable() {
        List<String> result = CollectionUtils.toMutableList(Arrays.asList("a", "b", "c"));
        result.add("x");

        assertEquals(4, result.size());
    }

    @Test
    public void toArrayEmptyIterable() {
        Iterable<String> iterable = Collections.emptyList();

        String[] result = CollectionUtils.toArray(iterable, String.class);

        assertEquals(iterable, Arrays.asList(result));
    }

    @Test
    public void toArrayNonEmptyIterable() {
        Iterable<String> iterable = Arrays.asList("a", "b", "c");

        String[] result = CollectionUtils.toArray(iterable, String.class);

        assertEquals(iterable, Arrays.asList(result));
    }

    @Test
    public void toArrayEmptyCollection() {
        Collection<String> iterable = Collections.emptyList();

        String[] result = CollectionUtils.toArray(iterable, String.class);

        assertEquals(iterable, Arrays.asList(result));
    }

    @Test
    public void toArrayNonEmptyCollection() {
        Collection<String> iterable = Arrays.asList("a", "b", "c");

        String[] result = CollectionUtils.toArray(iterable, String.class);

        assertEquals(iterable, Arrays.asList(result));
    }
}
