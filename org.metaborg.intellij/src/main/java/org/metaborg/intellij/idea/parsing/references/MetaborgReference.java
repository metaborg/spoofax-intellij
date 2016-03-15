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

package org.metaborg.intellij.idea.parsing.references;

import com.intellij.psi.*;
import org.jetbrains.annotations.*;

/**
 * A reference to a definition.
 */
public abstract class MetaborgReference extends PsiReferenceBase<MetaborgReferenceElement> {

    /**
     * Initializes a new instance of the {@link MetaborgReference} class.
     *
     * @param reference The reference.
     * @param isSoft    Whether the reference is a soft reference.
     */
    protected MetaborgReference(final MetaborgReferenceElement reference, final boolean isSoft) {
        super(reference, isSoft);
    }

    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public abstract PsiElement resolve();

    /**
     * {@inheritDoc}
     */
    @Override
    public final Object[] getVariants() {
        return new Object[0];
    }

}
