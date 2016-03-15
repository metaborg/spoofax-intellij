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

import com.intellij.openapi.util.*;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.*;
import com.intellij.util.*;

public final class MetaborgIdentifierManipulator extends AbstractElementManipulator<SpoofaxIdentifier> {
    @Override
    public SpoofaxIdentifier handleContentChange(
            final SpoofaxIdentifier element, final TextRange range, final String newContent) throws
            IncorrectOperationException {

        final String oldText = element.getText();
        final String newText = oldText.substring(
                0,
                range.getStartOffset()
        ) + newContent + oldText.substring(range.getEndOffset());
        final PsiElement child = element.getFirstChild();
        if (child instanceof LeafPsiElement) {
            ((LeafPsiElement)child).replaceWithText(newText);
            return element;
        }
        throw new IncorrectOperationException("Bad PSI.");
    }
}
