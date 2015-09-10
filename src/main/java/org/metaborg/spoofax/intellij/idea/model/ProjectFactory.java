package org.metaborg.spoofax.intellij.idea.model;

import com.google.common.base.Preconditions;
import com.intellij.openapi.module.Module;
import org.apache.commons.vfs2.FileObject;

public final class ProjectFactory {

    //private final IIntelliJResourceService resourceService;

    /**
     * Creates a new instance of the @link{ProjectFactory} class.
     */
    //@Inject
    public ProjectFactory() //IIntelliJResourceService resourceService)
    {
        //Preconditions.checkNotNull(resourceService);

        //this.resourceService = resourceService;
    }

    /**
     * Creates a new @link{IntelliJProject}.
     *
     * @param intellijModule The IntelliJ module.
     * @param contentPath Where the module lives.
     */
    public IntelliJProject create(Module intellijModule, FileObject contentPath)
    {
        Preconditions.checkNotNull(intellijModule);

        return new IntelliJProject(intellijModule, contentPath);
    }
}
