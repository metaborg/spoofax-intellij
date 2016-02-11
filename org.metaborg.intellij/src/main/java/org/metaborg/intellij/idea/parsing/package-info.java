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

/**
 * Parsing functionality.
 *
 * IntelliJ IDEA uses three concepts for parsing: the highlighter, the lexer and the
 * parser.
 *
 * The highlighter is an incremental lexer, and IntelliJ IDEA will call upon it for syntax
 * highlighting. It may ask the highlighter to highlight only a subsection of the source file,
 * usually only the part that's most likely to change due to user actions such as typing.
 * The {@link org.metaborg.intellij.idea.parsing.SpoofaxHighlightingLexer} class is the
 * highlighting lexer for Spoofax languages.
 *
 * Under the hood IntelliJ IDEA builds an in-memory representation of each source file.
 * It does this by invoking the lexer on the whole file, and then the parser on the
 * tokens returned by the lexer to construct a PSI tree from it.  For a source file the
 * PSI tree is similar to an AST, but in IntelliJ IDEA the PSI elements are also used
 * to describe things other than syntax, such as the source files themselves
 * ({@link com.intellij.psi.PsiFile}).
 *
 * For Spoofax languages we use the {@link CharacterLexer} class as the lexer. It simply
 * returns each character in the source as a token, as the SGLR parser works on characters
 * and has no actual lexer. There is no actual parser implemented for Spoofax languages,
 * as the IntelliJ IDEA framework for parsers is too limited. Instead, we override the
 * {@link com.intellij.psi.tree.IFileElementType#doParseContents} method and do the
 * parsing ourselves.
 *
 *
 */
@NonNullByDefault
package org.metaborg.intellij.idea.parsing;

import org.metaborg.intellij.*;