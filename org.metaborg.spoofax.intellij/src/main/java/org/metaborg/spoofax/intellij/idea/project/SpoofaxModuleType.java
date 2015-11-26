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

package org.metaborg.spoofax.intellij.idea.project;


import com.google.inject.Inject;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.Sdk;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;

import javax.swing.*;

/**
 * The type of a Spoofax module.
 */
public final class SpoofaxModuleType extends ModuleType<SpoofaxModuleBuilder> {

    // The module ID. This is displayed to the user when the ModuleType cannot be found.
    @NotNull
    public static final String ID = "SPOOFAX_MODULE"; // This is also used in plugin.xml.
    @NotNull
    private static final String NAME = "Spoofax";
    @NotNull
    private static final String DESCRIPTION = "Spoofax Module";

    /**
     * This instance is created by IntelliJ's plugin system.
     * Do not call this method manually.
     */
    public SpoofaxModuleType() {
        super(ID);
        IdeaPlugin.injector().injectMembers(this);
    }

    // TODO: Remove

    /**
     * Gets the module type.
     *
     * @return The module type.
     */
    @NotNull
    public static ModuleType getModuleType() {
        return ModuleTypeManager.getInstance().findByID(ID);
    }

    @Inject
    private void inject() {
    }

    /**
     * Creates a module builder.
     *
     * @return The created module builder.
     */
    @NotNull
    @Override
    public SpoofaxModuleBuilder createModuleBuilder() {
        return IdeaPlugin.injector().getInstance(SpoofaxModuleBuilder.class);
    }

    /**
     * Gets the name of the module type.
     *
     * @return The name of the module type.
     */
    @NotNull
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Gets the description of the module type.
     *
     * @return The description.
     */
    @NotNull
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * Gets the big icon for this module type.
     *
     * @return The big icon.
     */
    @Override
    public Icon getBigIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

    /**
     * Gets the icon for this module type.
     *
     * @param isOpened Whether the module is expanded.
     * @return The icon.
     */
    @Override
    public Icon getNodeIcon(@Deprecated boolean isOpened) {
        return SpoofaxIcons.INSTANCE.Default;
    }

//    /**
//     * Creates wizard steps for the module builder.
//     *
//     * @param wizardContext   The wizard context.
//     * @param moduleBuilder   The module builder.
//     * @param modulesProvider The modules provider.
//     * @return An array of wizard steps.
//     */
//    @NotNull
//    @Override
//    public ModuleWizardStep[] createWizardSteps(
//            @NotNull WizardContext wizardContext,
//            @NotNull SpoofaxModuleBuilder moduleBuilder,
//            @NotNull ModulesProvider modulesProvider) {
//        ArrayList<ModuleWizardStep> steps = new ArrayList<>();
//
//        steps.add(new MetaborgModuleWizardStep(moduleBuilder, wizardContext));
//
//        final ModuleWizardStep[] wizardSteps = steps.toArray(new ModuleWizardStep[steps.size()]);
//        return ArrayUtil.mergeArrays(wizardSteps,
//                                     super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider));
//    }

//    @Nullable
//    @Override
//    public ModuleWizardStep modifyProjectTypeStep(
//            @NotNull SettingsStep settingsStep,
//            @NotNull ModuleBuilder moduleBuilder) {
//        return ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep,
//                                                                             moduleBuilder,
//                                                                             sdk -> moduleBuilder.isSuitableSdkType(sdk));
//    }


    @Override
    public boolean isValidSdk(@NotNull Module module, Sdk projectSdk) {
        return super.isValidSdk(module, projectSdk);
    }
}