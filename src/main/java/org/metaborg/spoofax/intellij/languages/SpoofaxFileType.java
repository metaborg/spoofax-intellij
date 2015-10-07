package org.metaborg.spoofax.intellij.languages;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguage;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxIcons;
import org.metaborg.spoofax.intellij.logging.InjectLogger;
import org.slf4j.Logger;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for all Spoofax file types.
 */
public abstract class SpoofaxFileType extends LanguageFileType {

    /**
     * Initializes a new instance of the {@link SpoofaxFileType} class.
     *
     * @param language The language.
     */
    protected SpoofaxFileType(@NotNull final SpoofaxIdeaLanguage language) {
        super(language);
    }

    /**
     * Gets the Spoofax language.
     *
     * @return The Spoofax language.
     */
    public final ILanguage getSpoofaxLanguage() {
        return ((SpoofaxIdeaLanguage)super.getLanguage()).language();
    }

    @NotNull
    @Override
    public String getName() {
        return this.getSpoofaxLanguage().name();
    }

    @NotNull
    @Override
    public String getDescription() {
        // TODO:
        return "A spoofax language";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return LanguageUtils.getDefaultExtension(this.getSpoofaxLanguage().activeImpl());
    }

    @NotNull
    public Set<String> getExtensions() {
        return LanguageUtils.getExtensions(this.getSpoofaxLanguage().activeImpl());
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

}
