package org.metaborg.spoofax.intellij.meta;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intellij.compiler.server.impl.BuildProcessClasspathManager;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.*;
import org.metaborg.core.resource.IResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Manages the meta languages.
 */
@Singleton
public final class MetaLanguageManager {
    private final Logger logger = LoggerFactory.getLogger(MetaLanguageManager.class);

    private final ILanguageService languageService;
    private final ILanguageDiscoveryService discoveryService;
    private final IResourceService resourceService;

    @Inject
    private MetaLanguageManager(ILanguageService languageService, ILanguageDiscoveryService discoveryService, IResourceService resourceService) {
        this.languageService = languageService;
        this.discoveryService = discoveryService;
        this.resourceService = resourceService;
    }

    public void loadLanguages() {
        loadLanguage("org.metaborg.meta.lang.nabl");
    }

    private void loadLanguage(String id) {
        URL url = this.getClass().getClassLoader().getResource(id + ".spoofax-language");
        if (url == null)
        {
            logger.error("Meta language '" + id + "' could not be resolved to a class path.");
            return;
        }
        String zipUri = "zip://" + url.getPath();
        FileObject file = this.resourceService.resolve(zipUri);
        try {
            if (!file.exists())
            {
                logger.error("Meta language '" + id + "' does not exist in classpath at: " + file.toString());
                return;
            }
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        try {
            final Iterable<ILanguageComponent> discovery = this.discoveryService.discover(file);
            List<ILanguageImpl> lis = new ArrayList<ILanguageImpl>();
            for (ILanguageComponent c : discovery)
            {
                for (ILanguageImpl li : c.contributesTo())
                {
                    lis.add(li);
                }
            }
            logger.info("For '" + id + "' loaded languages: " + Joiner.on(", ").join(lis));
        } catch (MetaborgException e) {
            e.printStackTrace();
        }

    }
}
