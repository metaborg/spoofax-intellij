package org.metaborg.spoofax.intellij.factories;

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.Extensions;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.StringFormatter;

/**
 * Provides an instance from the IntelliJ extensions.
 */
/* package private */ final class IntelliJExtensionProvider<T> implements Provider<T> {

    @NotNull
    private final Class<T> extensionClass;
    @NotNull
    private final String extensionPointName;

    /**
     * Initializes a new instance of the {@link IntelliJExtensionProvider} class.
     *
     * @param extensionClass The class of the extension to load.
     * @param extensionPointName The extension point name.
     */
    /* package private */ IntelliJExtensionProvider(@NotNull final Class<T> extensionClass, @NotNull final String extensionPointName) {
        this.extensionClass = extensionClass;
        this.extensionPointName = extensionPointName;
    }

    @Override
    @NotNull
    public T get() {
        Object[] candidates = Extensions.getExtensions(this.extensionPointName);

        for (Object candidate : candidates) {
            if (candidate.getClass().equals(this.extensionClass)) {
                return (T)candidate;
            }
        }
        throw new ProvisionException(StringFormatter.format("No extensions are registered for the class {} in extension point {}.",
                                                            this.extensionClass, this.extensionPointName));
    }
}
