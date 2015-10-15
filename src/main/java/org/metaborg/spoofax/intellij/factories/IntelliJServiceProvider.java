package org.metaborg.spoofax.intellij.factories;

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.StringFormatter;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Provides an instance from the IntelliJ {@link com.intellij.openapi.components.ServiceManager}.
 */
/* package private */ final class IntelliJServiceProvider<T> implements Provider<T> {

    @NotNull
    private final Class<T> service;

    /**
     * Initializes a new instance of the {@link IntelliJServiceProvider} class.
     *
     * @param service The class of the service to load.
     */
    /* package private */ IntelliJServiceProvider(@NotNull final Class<T> service) {
        this.service = service;
    }

    @Override
    @NotNull
    public T get() {
        return ServiceManager.getService(this.service);
    }
}
