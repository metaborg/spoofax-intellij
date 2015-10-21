package org.metaborg.spoofax.intellij.idea.project;

import org.jetbrains.annotations.NotNull;
import org.metaborg.core.language.ILanguage;
import org.metaborg.core.language.ILanguageImpl;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Item in the settings dialog.
 * <p>
 * Note that this item is mutable.
 */
public final class LanguageImplItem {

    @NotNull
    private final ILanguage language;
    @Nullable
    private ILanguageImpl currentImplementation;

    public LanguageImplItem(@NotNull ILanguage language) {
        this.language = language;
    }

    @NotNull
    public final ILanguage language() { return this.language; }

    @Nullable
    public final ILanguageImpl currentImplementation() { return this.currentImplementation; }

    @NotNull
    public final List<ILanguageImpl> getImplementations() {
        ArrayList<ILanguageImpl> implementations = new ArrayList<ILanguageImpl>();
        implementations.add(null);
        for (ILanguageImpl impl : this.language.impls()) {
            implementations.add(impl);
        }
        return implementations;
    }

    public final void setCurrentImplementation(@Nullable final ILanguageImpl currentImplementation) {
        this.currentImplementation = currentImplementation;
    }
}
