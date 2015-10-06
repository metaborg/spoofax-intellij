package org.metaborg.spoofax.intellij.idea.model;


import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;

import javax.swing.*;
import java.util.ArrayList;

/**
 * The type of a Spoofax module.
 */
public final class SpoofaxModuleType extends ModuleType<SpoofaxModuleBuilder> {

    @NotNull
    public static final String ID = "SPOOFAX_MODULE"; // This is also used in plugin.xml.
    @NotNull
    private static final String NAME = "Spoofax";
    @NotNull
    private static final String DESCRIPTION = "Spoofax Module";

    public SpoofaxModuleType() {
        super(ID);
    }

    @NotNull
    public static ModuleType getModuleType() {
        return ModuleTypeManager.getInstance().findByID(ID);
    }

    @NotNull
    @Override
    public SpoofaxModuleBuilder createModuleBuilder() {
        return IdeaPlugin.injector().getInstance(SpoofaxModuleBuilder.class);
    }

    @NotNull
    @Override
    public String getName() {
        return NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public Icon getBigIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    @Override
    public Icon getNodeIcon(@Deprecated boolean isOpened) {
        return SpoofaxIcons.INSTANCE.Default;
    }

    @NotNull
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext,
                                                @NotNull SpoofaxModuleBuilder moduleBuilder,
                                                @NotNull ModulesProvider modulesProvider) {
        ArrayList<ModuleWizardStep> steps = new ArrayList<>();

        steps.add(new PageOneWizardStep());
        //steps.add(new PageTwoWizardStep(moduleBuilder));

        final ModuleWizardStep[] wizardSteps = steps.toArray(new ModuleWizardStep[steps.size()]);
        return ArrayUtil.mergeArrays(wizardSteps,
                                     super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider));
    }

    @Nullable
    @Override
    public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep,
                                                  @NotNull ModuleBuilder moduleBuilder) {
        return ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep, moduleBuilder, o -> true);
    }

    @Override
    public boolean isValidSdk(@NotNull Module module, Sdk projectSdk) {
        return super.isValidSdk(module, projectSdk);
    }
}