package org.metaborg.spoofax.intellij.idea;

import com.google.inject.Inject;
import com.intellij.compiler.impl.BuildTargetScopeProvider;
import com.intellij.openapi.compiler.CompileScope;
import com.intellij.openapi.compiler.CompilerFilter;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.api.CmdlineProtoUtil;
import org.jetbrains.jps.api.CmdlineRemoteProto;
import org.metaborg.spoofax.intellij.SpoofaxTargetType;

import java.util.Collections;
import java.util.List;

/**
 * Controls the list of build targets when Make is invoked for a particular scope.
 */
public class SpoofaxBuildTargetScopeProvider extends BuildTargetScopeProvider {

    private SpoofaxTargetType targetType;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxBuildTargetScopeProvider() {
        IdeaPlugin.injectMembers(this);
    }

    @Inject @SuppressWarnings("unused")
    private void inject(SpoofaxTargetType targetType) {
        this.targetType = targetType;
    }

    @NotNull
    @Override
    public List<CmdlineRemoteProto.Message.ControllerMessage.ParametersMessage.TargetTypeBuildScope> getBuildTargetScopes(CompileScope baseScope, CompilerFilter filter, Project project, boolean forceBuild) {
        return Collections.singletonList(CmdlineProtoUtil.createAllTargetsScope(this.targetType, forceBuild));
    }
}
