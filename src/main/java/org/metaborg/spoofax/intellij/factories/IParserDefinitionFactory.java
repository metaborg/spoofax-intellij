package org.metaborg.spoofax.intellij.factories;

import com.intellij.lang.ParserDefinition;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.idea.languages.SpoofaxFileType;

/**
 * Factory for parser definitions.
 */
public interface IParserDefinitionFactory {

    /**
     * Creates a new parser definition for the specified file type.
     *
     * @param fileType The file type.
     * @return The created parser definition.
     */
    @NotNull
    ParserDefinition create(@NotNull SpoofaxFileType fileType);

}
