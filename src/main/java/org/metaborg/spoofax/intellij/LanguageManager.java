package org.metaborg.spoofax.intellij;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.resource.IResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 9/11/15.
 */
@Singleton
public final class LanguageManager {
    private final Logger logger = LoggerFactory.getLogger(LanguageManager.class);

    private final ILanguageService languageService;
    private final ILanguageDiscoveryService discoveryService;
    private final IResourceService resourceService;

    @Inject
    private LanguageManager(ILanguageService languageService, ILanguageDiscoveryService discoveryService, IResourceService resourceService) {
        this.languageService = languageService;
        this.discoveryService = discoveryService;
        this.resourceService = resourceService;
    }

    public void loadMetaLanguages() {
        loadLanguage("org.metaborg.meta.lang.esv");
        loadLanguage("org.metaborg.meta.lang.sdf");
        loadLanguage("org.metaborg.meta.lang.stratego");
        loadLanguage("org.metaborg.meta.lib.analysis");
    }

    private void loadLanguage(String id) {
        URL url = this.getClass().getClassLoader().getResource("meta-languages/" + id + ".spoofax-language");
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
