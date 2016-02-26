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

package org.metaborg.intellij.jps.builders;

import com.google.inject.*;
import com.intellij.compiler.impl.*;
import com.intellij.facet.*;
import com.intellij.openapi.compiler.*;
import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.*;
import org.jetbrains.jps.api.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.facets.*;
import org.metaborg.intellij.jps.*;
import org.jetbrains.jps.api.CmdlineRemoteProto.Message.ControllerMessage.ParametersMessage.TargetTypeBuildScope;

import java.util.*;

/**
 * Controls the list of build target builders when Make is invoked for a particular scope.
 */
public final class SpoofaxBuildTargetScopeProvider extends BuildTargetScopeProvider {

    private SpoofaxPostTargetType postTargetType;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public SpoofaxBuildTargetScopeProvider() {
        super();
//        SpoofaxIdeaPlugin.injector().injectMembers(this);
        SpoofaxJpsPlugin.injector().injectMembers(this);
    }

    @SuppressWarnings("unused")
    @Inject
    private void inject(final SpoofaxPostTargetType postTargetType) {
        this.postTargetType = postTargetType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TargetTypeBuildScope> getBuildTargetScopes(
            final CompileScope baseScope,
            @SuppressWarnings("deprecation") final CompilerFilter filter,
            final Project project,
            final boolean forceBuild) {

        // Does any of the modules in the project have the Metaborg facet?
        if (!ProjectFacetManager.getInstance(project).hasFacets(MetaborgFacet.ID)) {
            // No, therefore we will not mess with the build.
            return Collections.emptyList();
        }

        // Gather the target IDs (module names) of the target modules.
        final List<String> targetIds = new ArrayList<>();
        for (final Module module : baseScope.getAffectedModules()) {
            final MetaborgFacet facet = MetaborgFacet.getInstance(module);
            if (facet == null) continue;

            targetIds.add(module.getName());
        }

        // Create a new TargetTypeBuildScope that uses the specified target type to build the target modules.
        return Collections.singletonList(
                CmdlineProtoUtil.createTargetsScope(this.postTargetType.getTypeId(), targetIds, forceBuild)
        );
    }
}
