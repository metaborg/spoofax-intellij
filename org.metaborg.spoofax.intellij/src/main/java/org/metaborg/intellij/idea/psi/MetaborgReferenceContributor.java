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

import com.google.inject.Inject;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.metaborg.spoofax.intellij.idea.SpoofaxIdeaPlugin;

/**
 * Contributes reference providers for PSI elements that match a certain pattern.
 * <p>
 * This contributor is run once per project.
 */
public final class MetaborgReferenceContributor extends PsiReferenceContributor {

    private IMetaborgReferenceProviderFactory referenceProviderFactory;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public MetaborgReferenceContributor() {
        super();
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

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
