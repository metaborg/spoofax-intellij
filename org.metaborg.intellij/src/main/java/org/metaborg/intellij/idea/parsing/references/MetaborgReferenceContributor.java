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

import com.google.inject.Inject;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;

/**
 * Contributes reference providers for PSI elements that match a certain pattern.
 * <p>
 * This contributor is run once per project.
 */
public final class MetaborgReferenceContributor extends PsiReferenceContributor {

    private IMetaborgReferenceProviderFactory referenceProviderFactory;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public MetaborgReferenceContributor() {
        super();
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @SuppressWarnings("unused")
    @Inject
    private void inject(final IMetaborgReferenceProviderFactory referenceProviderFactory) {
        this.referenceProviderFactory = referenceProviderFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerReferenceProviders(final PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(MetaborgReferenceElement.class),
                this.referenceProviderFactory.create()
        );
    }
}
