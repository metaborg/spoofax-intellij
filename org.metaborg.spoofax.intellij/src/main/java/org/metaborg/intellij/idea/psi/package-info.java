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

/**
 * Functionality for PSI trees in IntelliJ IDEA.
 * <p>
 * The PSI tree is the AST format used by IntelliJ. It's used to represent not just syntax,
 * but also the files themselves.
 * <p>
 * <h3>PSI elements</h3>
 * The common base interface of all PSI elements is {@link com.intellij.psi.PsiElement}.
 * See <a href="http://www.jetbrains.org/intellij/sdk/docs/basics/architectural_overview.html">IntelliJ
 * IDEA Architectural Overview</a> for a high-level overview.
 */
@NonNullByDefault
package org.metaborg.intellij.idea.psi;

import org.metaborg.core.NonNullByDefault;