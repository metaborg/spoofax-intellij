package org.metaborg.spoofax.intellij.languages;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageExtensionPoint;
import com.intellij.util.KeyedLazyInstanceEP;
import org.jetbrains.annotations.NotNull;

public final class InstanceLanguageExtensionPoint<T> extends LanguageExtensionPoint<T> {
    @NotNull
    private final T instance;

    public InstanceLanguageExtensionPoint(Language language, T instance) {
        this.instance = instance;
        this.language = language.getID();
        this.implementationClass = null;
    }

    @NotNull
    @Override
    public final T getInstance() {
        return this.instance;
    }
}
