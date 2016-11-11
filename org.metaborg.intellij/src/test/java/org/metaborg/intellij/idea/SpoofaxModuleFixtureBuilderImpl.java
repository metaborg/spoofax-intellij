package org.metaborg.intellij.idea;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.testFramework.builders.ModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.ModuleFixture;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import com.intellij.testFramework.fixtures.impl.ModuleFixtureBuilderImpl;
import com.intellij.testFramework.fixtures.impl.ModuleFixtureImpl;
import org.jetbrains.annotations.NotNull;
import org.metaborg.intellij.idea.projects.MetaborgModuleBuilder;
import org.metaborg.intellij.idea.projects.MetaborgModuleType;

public class SpoofaxModuleFixtureBuilderImpl extends ModuleFixtureBuilderImpl<ModuleFixture> implements SpoofaxModuleFixtureBuilder {

    private static final MetaborgModuleType MODULE_TYPE = new MetaborgModuleType();

    public SpoofaxModuleFixtureBuilderImpl(TestFixtureBuilder<? extends IdeaProjectTestFixture> fixtureBuilder) {
        super(MODULE_TYPE, fixtureBuilder);
    }

    @Override
    protected void setupRootModel(ModifiableRootModel rootModel) {
//        MetaborgModuleBuilder metaborgModuleBuilder = new MetaborgModuleBuilder();
        MetaborgModuleBuilder builder = MODULE_TYPE.createModuleBuilder();
        try {
            builder.setContentEntryPath(rootModel.getContentRoots()[0].getPath());
            builder.setupRootModel(rootModel);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
//        super.setupRootModel(rootModel);
    }

//    @Override
//    protected Module createModule() {
//        return super.createModule();
//        MODULE_TYPE.createModuleBuilder().
//    }

    @Override
    protected ModuleFixture instantiateFixture() {
        return new ModuleFixtureImpl(this);
    }
}
