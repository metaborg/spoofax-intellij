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

package org.metaborg.intellij.resources;

import com.intellij.openapi.vfs.*;
import com.intellij.psi.*;
import org.apache.commons.vfs2.*;
import org.metaborg.core.resource.*;

import javax.annotation.*;

/**
 * IntelliJ resource service.
 */
public interface IIntelliJResourceService extends IResourceService {

    /**
     * Converts an IntelliJ resource into a VFS resource.
     *
     * @param resource The IntelliJ resource to convert.
     * @return The corresponding VFS resource.
     */
    @Nullable FileObject resolve(VirtualFile resource);

    /**
     * Converts an IntelliJ PSI file into a VFS resource.
     *
     * @param file The PSI file.
     * @return The corresponding VFS resource; or <code>null</code>.
     */
    @Nullable FileObject resolve(PsiFile file);

    /**
     * Converts a VFS resource into an IntelliJ resource, if possible.
     *
     * @param resource The VFS resource to convert.
     * @return The corresponding IntelliJ resource,
     * or <code>null</code> if it could not be converted.
     */
    @Nullable
    VirtualFile unresolve(FileObject resource);

}
