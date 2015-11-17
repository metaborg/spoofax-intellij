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

package org.metaborg.idea.psi;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import org.apache.commons.vfs2.FileObject;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.analysis.AnalysisFileResult;
import org.metaborg.core.processing.analyze.IAnalysisResultRequester;
import org.metaborg.core.source.ISourceLocation;
import org.metaborg.core.tracing.IResolverService;
import org.metaborg.core.tracing.Resolution;
import org.metaborg.spoofax.intellij.idea.psi.SpoofaxPsiVisitor;
import org.metaborg.spoofax.intellij.idea.psi.SpoofaxReference;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;
import org.spoofax.interpreter.terms.IStrategoTerm;

import java.util.ArrayList;
import java.util.List;

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