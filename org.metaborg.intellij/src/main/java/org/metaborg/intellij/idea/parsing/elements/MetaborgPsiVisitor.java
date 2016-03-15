/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.metaborg.intellij.idea.parsing.elements;

import com.intellij.psi.*;

/**
 * Visitor for Spoofax PSI elements.
 */
public final class MetaborgPsiVisitor extends PsiElementVisitor {

    /**
     * Visits a property.
     *
     * @param property The property.
     */
    public void visitProperty(final MetaborgPsiElement property) {
        visitPsiElement(property);
    }

    /**
     * Visits a PSI element.
     *
     * @param element The element.
     */
    public void visitPsiElement(final PsiElement element) {
        visitElement(element);
    }

    /**
     * Visits a property.
     *
     * @param property The property.
     */
    public void visitProperty(final SpoofaxIdentifier property) {
        visitPsiElement(property);
    }

}
