package org.metaborg.spoofax.intellij.idea.project;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.roots.ui.configuration.ClasspathEditor;
import com.intellij.openapi.roots.ui.configuration.CommonContentEntriesEditor;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationEditorProvider;
import com.intellij.openapi.roots.ui.configuration.ModuleConfigurationState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;
import org.metaborg.spoofax.intellij.factories.ILanguageImplEditorFactory;
import org.metaborg.spoofax.intellij.factories.IProjectFactory;
import org.metaborg.spoofax.intellij.idea.IIntelliJProjectService;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxModuleType;
import org.metaborg.spoofax.intellij.resources.IIntelliJResourceService;

/**
 * Provides editors for the module's settings in the <em>Project Structure</em> dialog.
 */
@Singleton
public final class SpoofaxModuleConfigurationEditorProvider implements ModuleConfigurationEditorProvider {

    @NotNull
    private SpoofaxModuleType spoofaxModuleType;
    @NotNull
    private ILanguageImplEditorFactory languageImplEditorFactory;

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxModuleConfigurationEditorProvider() {
        IdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    private void inject(@NotNull final SpoofaxModuleType spoofaxModuleType, @NotNull final ILanguageImplEditorFactory languageImplEditorFactory) {
        this.spoofaxModuleType = spoofaxModuleType;
        this.languageImplEditorFactory = languageImplEditorFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ModuleConfigurationEditor[] createEditors(@NotNull final ModuleConfigurationState state) {
        final Module module = state.getRootModel().getModule();
        final ModuleType moduleType = ModuleType.get(module);
        if (moduleType != this.spoofaxModuleType) {
            return ModuleConfigurationEditor.EMPTY;
        }
        return new ModuleConfigurationEditor[] {
            new CommonContentEntriesEditor(module.getName(), state, JavaSourceRootType.SOURCE, JavaSourceRootType.TEST_SOURCE),
            new ClasspathEditor(state),
            this.languageImplEditorFactory.create(state),
        };
    }
}
