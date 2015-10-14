package org.metaborg.spoofax.intellij.factories;

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import org.jetbrains.annotations.NotNull;
import org.metaborg.spoofax.intellij.StringFormatter;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Provides an instance from the Java {@link java.util.ServiceLoader}.
 */
/* package private */ final class JavaServiceProvider<T> implements Provider<T> {

    @NotNull
    private final Class<T> service;

    /**
     * Initializes a new instance of the {@link JavaServiceProvider} class.
     *
     * @param service The class of the service to load.
     */
    /* package private */ JavaServiceProvider(@NotNull final Class<T> service) {
        this.service = service;
    }

    @Override
    @NotNull
    public T get() {
        final ServiceLoader<T> loader = ServiceLoader.load(this.service);
        final Iterator<T> iterator = loader.iterator();

        if (!iterator.hasNext())
            throw new ProvisionException(StringFormatter.format("No implementations are registered for the class {}.", this.service));

        final T obj = iterator.next();

        if (iterator.hasNext())
            throw new ProvisionException(StringFormatter.format("Multiple implementations are registered for the class {}.", this.service));

        return obj;
    }
}
