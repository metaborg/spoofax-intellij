package org.metaborg.spoofax.intellij.idea.model;


import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;

/**
 * The type of a Spoofax module.
 */
public class SpoofaxModuleType extends ModuleType<SpoofaxModuleBuilder> {

    public static final String ID = "SPOOFAX_MODULE"; // This is also used in plugin.xml.
    private static final String NAME = "Spoofax";
    private static final String DESCRIPTION = "Spoofax Module";

    public static ModuleType getModuleType() {
        return ModuleTypeManager.getInstance().findByID(ID);
    }

    public SpoofaxModuleType()
    {
        super(ID);
    }

    @NotNull
    @Override
    public SpoofaxModuleBuilder createModuleBuilder() {
        return new SpoofaxModuleBuilder();
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

    @Override
    public boolean isValidSdk(@NotNull Module module, Sdk projectSdk) {
        return super.isValidSdk(module, projectSdk);
    }

    @Nullable
    @Override
    public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep, @NotNull ModuleBuilder moduleBuilder) {
        return ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep, moduleBuilder, o -> true);
    }

    @NotNull
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull SpoofaxModuleBuilder moduleBuilder, @NotNull ModulesProvider modulesProvider) {
        ArrayList<ModuleWizardStep> steps = new ArrayList<>();

        steps.add(new PageOneWizardStep());
        //steps.add(new PageTwoWizardStep(moduleBuilder));

        final ModuleWizardStep[] wizardSteps = steps.toArray(new ModuleWizardStep[steps.size()]);
        return ArrayUtil.mergeArrays(wizardSteps, super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider));
    }

    /*
    @Nullable
    @Override
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep, @NotNull ModuleBuilder moduleBuilder) {
        ModuleWizardStep step = ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep, moduleBuilder, o -> true);
        if (step != null) {
            step.setSourcePath("src" + File.separator + "main" + File.separator + "spoofax");
        }
        return step;
        return super.modifySettingsStep(settingsStep, moduleBuilder);
    }
    */
}