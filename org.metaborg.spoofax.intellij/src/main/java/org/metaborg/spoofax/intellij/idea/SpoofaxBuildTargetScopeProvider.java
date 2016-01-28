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

package org.metaborg.spoofax.intellij.idea;

import com.google.inject.Inject;
import com.intellij.compiler.impl.BuildTargetScopeProvider;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerFilter;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.api.CmdlineProtoUtil;
import org.jetbrains.jps.api.CmdlineRemoteProto.Message.ControllerMessage.ParametersMessage.TargetTypeBuildScope;
import org.metaborg.spoofax.intellij.jps.SpoofaxJpsPlugin;
import org.metaborg.intellij.jps.targetbuilders.SpoofaxPostTargetType;

import java.util.Collections;
import java.util.List;

/**
 * Controls the list of build target builders when Make is invoked for a particular scope.
 */
public final class SpoofaxBuildTargetScopeProvider extends BuildTargetScopeProvider {

    private SpoofaxPostTargetType postTargetType;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxBuildTargetScopeProvider() {
        SpoofaxJpsPlugin.injector().injectMembers(this);
    }

    @Inject
    private void inject(SpoofaxPostTargetType postTargetType) {
        this.postTargetType = postTargetType;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    @Override
    public List<TargetTypeBuildScope> getBuildTargetScopes(
            @NotNull final CompileScope baseScope,
            @SuppressWarnings("deprecation") @NotNull final CompilerFilter filter,
            @NotNull final Project project,
            boolean forceBuild) {
        return Collections.singletonList(CmdlineProtoUtil.createAllTargetsScope(postTargetType, forceBuild));
    }
}
