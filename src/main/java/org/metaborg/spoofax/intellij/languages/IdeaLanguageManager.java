package org.metaborg.spoofax.intellij.languages;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.util.proxy.ProxyFactory;
import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Manages languages and file types for IntelliJ IDEA.
 */
@Singleton
public final class IdeaLanguageManager {

    @InjectLogger
    private Logger logger;
    @NotNull
    private final ProxyFactory proxyFactory;
    @NotNull
    private final HashMap<ILanguage, SpoofaxIdeaLanguage> languages = new HashMap<>();
    @NotNull
    private final HashMap<ILanguage, SpoofaxFileType> fileTypes = new HashMap<>();

    /**
     * Gets a collection of IDEA languages.
     *
     * @return A collection of IDEA languages.
     */
    @NotNull
    public final Collection<SpoofaxIdeaLanguage> languages() {
        return this.languages.values();
    }

    /**
     * Gets a collection of file types.
     *
     * @return A collection of file types.
     */
    @NotNull
    public final Collection<SpoofaxFileType> fileTypes() {
        return this.fileTypes.values();
    }

    @Inject
    private IdeaLanguageManager() {
        this.proxyFactory = new ProxyFactory();
        //this.proxyFactory.setUseCache(false);
    }


    /**
     * Registers a Spoofax language.
     *
     * @param language The language to register.
     */
    public final void register(@NotNull final ILanguage language) {
        if (isRegistered(language))
            throw new IllegalArgumentException("Language '" + language + "' is already registered.");

        final SpoofaxIdeaLanguage ideaLanguage = createIdeaLanguage(language);
        final SpoofaxFileType fileType = createFileType(ideaLanguage);
        this.languages.put(language, ideaLanguage);
        this.fileTypes.put(language, fileType);
    }

    /**
     * Unregisters a Spoofax language.
     *
     * @param language The language to unregister.
     * @return <code>true</code> when the language was unregistered;
     * otherwise, <code>false</code> when the language was not found.
     */
    public final boolean unregister(@NotNull final ILanguage language) {
        this.fileTypes.remove(language);
        return this.languages.remove(language) != null;
    }

    /**
     * Checks whether a language is registered.
     *
     * @param language The language to test.
     * @return <code>true</code> when the language is registered;
     * otherwise, <code>false</code>.
     */
    public final boolean isRegistered(@NotNull final ILanguage language) {
        return this.languages.containsKey(language);
    }

    /**
     * Gets the file type for a Spoofax language.
     *
     * @param language The language.
     * @return The associated file type.
     */
    @NotNull
    public final SpoofaxFileType getFileType(@NotNull final ILanguage language) {
        if (!isRegistered(language))
            throw new IllegalArgumentException("Language '" + language + "' is not registered.");

        return this.fileTypes.get(language);
    }

    /**
     * Gets the IDEA language for a Spoofax language.
     *
     * @param language The language.
     * @return The associated IDEA language.
     */
    @NotNull
    public final SpoofaxIdeaLanguage getIdeaLanguage(@NotNull final ILanguage language) {
        if (!isRegistered(language))
            throw new IllegalArgumentException("Language '" + language + "' is not registered.");

        return this.languages.get(language);
    }

    /**
     * Creates a new IDEA language for a Spoofax language.
     *
     * @param language The language.
     * @return The created IDEA language.
     */
    @NotNull
    private final SpoofaxIdeaLanguage createIdeaLanguage(@NotNull final ILanguage language) {
        try {
            this.proxyFactory.setSuperclass(SpoofaxIdeaLanguage.class);
            return (SpoofaxIdeaLanguage)this.proxyFactory.create(new Class<?>[]{ ILanguage.class }, new Object[]{ language });
        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            this.logger.error("Unexpected unhandled exception.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new file type for an IDEA language.
     *
     * @param language The IDEA language.
     * @return The created file type.
     */
    @NotNull
    private final SpoofaxFileType createFileType(@NotNull final SpoofaxIdeaLanguage language) {
        try {
            this.proxyFactory.setSuperclass(SpoofaxFileType.class);
            return (SpoofaxFileType)this.proxyFactory.create(new Class<?>[]{ SpoofaxIdeaLanguage.class }, new Object[]{ language });
        } catch (NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            this.logger.error("Unexpected unhandled exception.", e);
            throw new RuntimeException(e);
        }
    }

}
