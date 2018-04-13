package org.metaborg.intellij.idea;

import com.intellij.openapi.project.Project;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.CodeInsightTestFixture;
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture;
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory;
import com.intellij.testFramework.fixtures.TestFixtureBuilder;
import com.intellij.util.ui.UIUtil;
import org.junit.After;
import org.junit.Before;

import javax.annotation.Nullable;

public abstract class SpoofaxTestCase {

    protected static IdeaProjectTestFixture fixture;
    protected static Project project;

    @Before
    public void setUp() throws Exception {
        final IdeaTestFixtureFactory factory = IdeaTestFixtureFactory.getFixtureFactory();
        final TestFixtureBuilder<IdeaProjectTestFixture> builder = factory.createFixtureBuilder(this.getClass().getName());

        fixture = builder.getFixture();
        fixture.setUp();

        project = fixture.getProject();
    }

    @After
    public void tearDown() throws Exception {
        UIUtil.invokeAndWaitIfNeeded(new Runnable() {
            @Override
            public void run() {
                try {
                    fixture.tearDown();
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
