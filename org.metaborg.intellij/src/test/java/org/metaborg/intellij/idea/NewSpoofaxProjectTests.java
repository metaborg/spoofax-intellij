package org.metaborg.intellij.idea;

import com.intellij.openapi.project.Project;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import com.intellij.util.ui.UIUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NewSpoofaxProjectTests extends CodeInsightFixtureTestCase<SpoofaxModuleFixtureBuilder> {


    @Override
    protected void setUp() throws Exception {
        final IdeaTestFixtureFactory factory = IdeaTestFixtureFactory.getFixtureFactory();
        factory.registerFixtureBuilder(SpoofaxModuleFixtureBuilder.class, SpoofaxModuleFixtureBuilderImpl.class);
        super.setUp();
    }

    @Override
    protected Class<SpoofaxModuleFixtureBuilder> getModuleBuilderClass() {
        return SpoofaxModuleFixtureBuilder.class;
    }

    // NOTE: Test names must start with `test`.

    public void testX() {

        System.out.println("Success!");
    }
}
