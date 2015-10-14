package org.metaborg.spoofax.intellij.idea;

import com.intellij.lang.Language;
import com.intellij.util.KeyedLazyInstanceEP;
import org.jetbrains.annotations.NotNull;

/**
 * Language extension point value wrapper.
 * <p>
 * This wrapper is used to provide an instance to a language extension point instead of a class.
 *
 * @param <T> The type of instance.
 */
public final class InstanceKeyedExtensionPoint<T> extends KeyedLazyInstanceEP<T> {
    @NotNull
    private final T instance;

    /**
     * Initializes a new instance of the {@link InstanceLanguageExtensionPoint} class.
     *
     * @param language The language.
     * @param instance The instance.
     */
    public InstanceKeyedExtensionPoint(@NotNull final Language language, @NotNull final T instance) {
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