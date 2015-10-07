package org.metaborg.spoofax.intellij.languages;

import com.intellij.lang.Language;
import com.intellij.util.KeyedLazyInstanceEP;
import org.jetbrains.annotations.NotNull;

public final class InstanceKeyedExtensionPoint<T> extends KeyedLazyInstanceEP<T> {
    @NotNull
    private final T instance;

    public InstanceKeyedExtensionPoint(Language language, T instance) {
        this.instance = instance;
        this.key = language.getID();
        this.implementationClass = null;
    }

    @NotNull
    @Override
    public final T getInstance() {
        return this.instance;
    }
}