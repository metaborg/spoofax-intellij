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

package org.metaborg.core.tracing;

import org.metaborg.core.MetaborgException;
import org.metaborg.core.syntax.ParseResult;

import javax.annotation.Nullable;

/**
 * Resolves references.
 */
public interface IReferenceResolver<T> {

    /**
     * Determines whether the specified term could be a reference.
     * <p>
     * This method should return <code>true</code> for all references, even invalid ones;
     * but return <code>false</code> for non-references.
     *
     * @param term   The term to check.
     * @param result The parse result.
     * @return <code>true</code> when the term could be a reference;
     * otherwise, <code>false</code>.
     * @throws MetaborgException
     */
    boolean isReference(T term, ParseResult<T> result) throws MetaborgException;

    /**
     * Attempts to resolve the reference of the specified term.
     *
     * @param term The term to resolve.
     * @return The {@link IResolution} if the reference resolution was successful;
     * otherwise, <code>null</code> if no resolution could be made.
     * @throws MetaborgException When reference resolution fails unexpectedly.
     */
    @Nullable
    IResolution<T> resolve(T term, ParseResult<T> result) throws MetaborgException;

}
