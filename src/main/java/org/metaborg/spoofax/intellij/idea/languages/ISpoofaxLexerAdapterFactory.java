package org.metaborg.spoofax.intellij.idea.languages;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguageImpl;

public interface ISpoofaxLexerAdapterFactory {
    @NotNull
    SpoofaxLexer create(@NotNull ILanguageImpl language, @NotNull SpoofaxTokenTypeManager tokenTypesManager);
}
