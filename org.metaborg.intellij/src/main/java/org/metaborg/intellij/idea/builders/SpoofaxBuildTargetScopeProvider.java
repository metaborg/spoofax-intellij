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

package org.metaborg.intellij.idea.builders;

import com.google.inject.*;
import com.intellij.compiler.impl.BuildTargetScopeProvider;
import com.intellij.facet.ProjectFacetManager;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerFilter;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import org.jetbrains.jps.api.CmdlineProtoUtil;
import org.jetbrains.jps.api.CmdlineRemoteProto.Message.ControllerMessage.ParametersMessage.TargetTypeBuildScope;
import org.metaborg.intellij.builders.TargetTypeConstants;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;
import org.metaborg.intellij.idea.facets.MetaborgFacet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Controls the list of build target builders when Make is invoked for a particular scope.
 */
public final class SpoofaxBuildTargetScopeProvider extends BuildTargetScopeProvider {

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this constructor manually.
     */
    public SpoofaxBuildTargetScopeProvider() {
        super();
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @SuppressWarnings("unused")
    @Inject
    private void inject() {

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
                CmdlineProtoUtil.createTargetsScope(TargetTypeConstants.PostTargetType, targetIds, forceBuild)
        );
    }
}
