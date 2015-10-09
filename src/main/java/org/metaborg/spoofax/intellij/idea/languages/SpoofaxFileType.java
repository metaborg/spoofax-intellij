package org.metaborg.spoofax.intellij.idea.languages;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;
import org.metaborg.spoofax.intellij.StringFormatter;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxIcons;
import org.metaborg.spoofax.intellij.languages.LanguageUtils;

import javax.swing.*;
import java.util.Set;

/**
 * A Spoofax language file type.
 *
 * There are no implementations of this class because it's instantiated dynamically.
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
        return StringFormatter.format("{} (Spoofax)", this.getSpoofaxLanguage().name());
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return LanguageUtils.getDefaultExtension(this.getSpoofaxLanguage());
    }

    @NotNull
    public Set<String> getExtensions() {
        return LanguageUtils.getExtensions(this.getSpoofaxLanguage());
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return SpoofaxIcons.INSTANCE.Default;
    }

}
