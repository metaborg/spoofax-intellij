package org.metaborg.spoofax.intellij.languages;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.MetaborgException;
import org.metaborg.core.language.ILanguageComponent;
import org.metaborg.core.language.ILanguageDiscoveryService;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.core.language.ILanguageService;
import org.metaborg.core.resource.IResourceService;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 9/11/15.
 */
@Singleton
public final class LanguageManager {
    @NotNull
    private final ILanguageService languageService;

    //@NotNull
    //private final Logger logger = LoggerFactory.getLogger(LanguageManager.class);
    @NotNull
    private final ILanguageDiscoveryService discoveryService;
    @NotNull
    private final IResourceService resourceService;
    @InjectLogger
    private Logger logger;

    @Inject
    private LanguageManager(@NotNull final ILanguageService languageService,
                            @NotNull final ILanguageDiscoveryService discoveryService,
                            @NotNull final IResourceService resourceService) {
        this.languageService = languageService;
        this.discoveryService = discoveryService;
        this.resourceService = resourceService;
    }

    public final void loadMetaLanguages() {
        loadLanguage("org.metaborg.meta.lang.esv-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.nabl-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.sdf-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.stratego-1.5.0-baseline-20150917-172646");
        loadLanguage("org.metaborg.meta.lang.template-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lang.ts-1.5.0-SNAPSHOT");
        loadLanguage("org.metaborg.meta.lib.analysis-1.5.0-SNAPSHOT");

        loadLanguage("org.metaborg.meta.lang.esv-1.5.0-baseline-20150905-200051");
        loadLanguage("org.metaborg.meta.lang.sdf-1.5.0-baseline-20150905-200051");
    }

    private final void loadLanguage(@NotNull final String id) {
        final URL url = this.getClass().getClassLoader().getResource("meta-languages/" + id + ".spoofax-language");
        if (url == null) {
            logger.error("Meta language '" + id + "' could not be resolved to a class path.");
            return;
        }
        final String zipUri = "zip://" + url.getPath();
        final FileObject file = this.resourceService.resolve(zipUri);
        try {
            if (!file.exists()) {
                logger.error("Meta language '" + id + "' does not exist in classpath at: " + file.toString());
                return;
            }
        } catch (FileSystemException e) {
            e.printStackTrace();
        }
        try {
            final Iterable<ILanguageComponent> discovery = this.discoveryService.discover(file);
            final List<ILanguageImpl> lis = new ArrayList<ILanguageImpl>();
            for (ILanguageComponent c : discovery) {
                for (ILanguageImpl li : c.contributesTo()) {
                    lis.add(li);
                }
            }
            logger.info("For '" + id + "' loaded languages: " + Joiner.on(", ").join(lis));
        } catch (MetaborgException e) {
            e.printStackTrace();
        }

    }
}
