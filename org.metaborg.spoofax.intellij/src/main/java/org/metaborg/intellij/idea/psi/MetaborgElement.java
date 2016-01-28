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

package org.metaborg.intellij.idea.psi;

import com.intellij.psi.PsiElement;

/**
 * A PSI element.
 * <p>
 * The Program Structure Interface (PSI) is a hierarchy of PSI elements
 * that represent the files and their structure (e.g. AST).
 */
public interface MetaborgElement extends PsiElement {

//    /**
//     * Initializes a new instance of the {@link MetaborgElement} class.
//     *
//     * @param node The AST node to wrap.
//     */
//    public MetaborgElement(final ASTNode node) {
//        super(node);
//    }
//
//    @Override
//    public PsiReference getReference() {
//        PsiReference[] references = getReferences();
//        return references.length == 0 ? null : references[0];
//    }
//
//    @NotNull
//    @Override
//    public PsiReference[] getReferences() {
//        return ReferenceProvidersRegistry.getReferencesFromProviders(this);
//    }

}