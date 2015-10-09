package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;

public interface ISpoofaxParserDefinitionFactory {
    @NotNull
    SpoofaxParserDefinition create(@NotNull SpoofaxFileType fileType);
}
