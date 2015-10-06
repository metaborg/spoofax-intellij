package org.metaborg.spoofax.intellij.logging;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.reflect.Field;

/**
 * Listens for injections in a type.
 */
public final class Slf4JTypeListener implements TypeListener {

    @Override
    public <I> void hear(@NotNull TypeLiteral<I> typeLiteral, @NotNull TypeEncounter<I> typeEncounter) {
        // Go through the class and its ancestors to find fields like this:
        //     @InjectLogger Logger logger;

        Class<?> clazz = typeLiteral.getRawType();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType() == Logger.class &&
                        field.isAnnotationPresent(InjectLogger.class)) {
                    typeEncounter.register(new Slf4JMembersInjector<>(field));
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
