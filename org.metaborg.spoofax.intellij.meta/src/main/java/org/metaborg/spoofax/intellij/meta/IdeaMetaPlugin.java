package org.metaborg.spoofax.intellij.meta;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.IdeaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IntelliJ IDEA meta languages plugin class.
 */
public final class IdeaMetaPlugin implements ApplicationComponent {

    final Logger logger = LoggerFactory.getLogger(IdeaMetaPlugin.class);

    protected static Injector injector = null;

    /**
     * Gets the injector.
     * @return The current injector.
     */
    public static Injector injector() {
        assert injector != null;

        return injector;
    }

    @Override
    public void initComponent() {
        injector = IdeaPlugin.injector().createChildInjector(new SpoofaxIdeaMetaDependencyModule());
        logger.error("Loaded Meta languages!");
    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return IdeaMetaPlugin.class.getName();
    }
}
