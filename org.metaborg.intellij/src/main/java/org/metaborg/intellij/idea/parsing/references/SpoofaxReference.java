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

import com.intellij.openapi.vfs.*;
import com.intellij.psi.*;
import org.jetbrains.annotations.*;
import org.metaborg.core.source.*;

/**
 * A reference to a definition.
 */
public final class SpoofaxReference extends MetaborgReference {

    private final ISourceRegion region;
    private final VirtualFile virtualFile;

    /**
     * Initializes a new instance of the {@link SpoofaxReference} class.
     *
     * @param reference The reference.
     */
    public SpoofaxReference(
            final MetaborgReferenceElement reference,
            final VirtualFile virtualFile,
            final ISourceRegion region) {
        super(reference, false);
        this.region = region;
        this.virtualFile = virtualFile;
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        @Nullable final PsiFile file = this.getElement().getManager().findFile(this.virtualFile);
        if (file == null)
            return null;
        return file.findElementAt(this.region.startOffset());
    }


}
