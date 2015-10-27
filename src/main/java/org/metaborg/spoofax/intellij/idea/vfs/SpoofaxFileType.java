package org.metaborg.spoofax.intellij.idea.vfs;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.metaborg.core.language.ILanguage;
import org.metaborg.spoofax.intellij.StringFormatter;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxIdeaLanguage;
import org.metaborg.spoofax.intellij.idea.model.SpoofaxIcons;
import org.metaborg.spoofax.intellij.languages.LanguageUtils;

import javax.swing.*;
import java.util.Set;

/**
 * A Spoofax language file type.
 * <p>
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
     * Gets the name of the file type.
     *
     * @return The name.
     */
    @NotNull
    @Override
    public String getName() {
        return this.getSpoofaxLanguage().name();
    }

    /**
     * Gets the Spoofax language.
     *
     * @return The Spoofax language.
     */
    public final ILanguage getSpoofaxLanguage() {
        return ((SpoofaxIdeaLanguage) super.getLanguage()).language();
    }

    /**
     * Gets the description of the file type.
     * <p>
     * This is shown in the <em>File types</em> settings dialog. If multiple file types have the same description,
     * the result of {@link #getName} is appended to the description.
     *
     * @return The description.
     */
    @NotNull
    @Override
    public String getDescription() {
        return StringFormatter.format("{} (Spoofax)", this.getSpoofaxLanguage().name());
    }

    /**
     * Gets the default extension of the file type.
     *
     * @return The default extension.
     */
    @NotNull
    @Override
    public String getDefaultExtension() {
        return LanguageUtils.getDefaultExtension(this.getSpoofaxLanguage());
    }

    /**
     * Gets the icon of the file type.
     *
     * @return The icon.
     */
    @Nullable
    @Override
    public Icon getIcon() {
        // TODO: Get icon from ILanguage facet, otherwise use default.
        return SpoofaxIcons.INSTANCE.Default;
    }

    /**
     * Gets the extensions recognized for this file type.
     *
     * @return A set of file extensions.
     */
    @NotNull
    public Set<String> getExtensions() {
        return LanguageUtils.getExtensions(this.getSpoofaxLanguage());
    }

}
