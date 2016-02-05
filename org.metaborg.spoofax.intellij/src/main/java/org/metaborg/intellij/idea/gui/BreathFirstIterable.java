/*
 * Copyright © 2015-2016
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

package org.metaborg.intellij.idea.gui;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Iterates over a tree.
 */
public abstract class BreathFirstIterable<E> implements Iterable<E> {

    private final BreathFirstIterator<E> iterator;
    protected BreathFirstIterable(@Nullable final E root) {
        this.iterator = new BreathFirstIterator<E>(this, root);
    }

    protected abstract Iterable<E> getChildren(E node);

    @Override
    public Iterator<E> iterator() {
        return iterator;
    }

    private static class BreathFirstIterator<E> implements Iterator<E> {
        private final Queue<E> queue = new LinkedList<>();
        private final BreathFirstIterable<E> iterable;

        protected BreathFirstIterator(final BreathFirstIterable<E> iterable, @Nullable final E root) {
            this.iterable = iterable;
            if (root != null)
                this.queue.add(root);
        }

        @Override
        public boolean hasNext() {
            return !this.queue.isEmpty();
        }

        @Override
        public E next() {
            final E node = this.queue.remove();
            final Iterable<E> children = this.iterable.getChildren(node);
            for (@Nullable final E child : children) {
                if (child != null)
                    this.queue.add(child);
            }
            return node;
        }
    }
}
