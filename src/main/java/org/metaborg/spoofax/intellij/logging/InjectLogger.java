package org.metaborg.spoofax.intellij.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a field that wants a logger to be injected.
 * <p>
 * For example:
 * <pre>{@code
 *
 * @InjectLogger private Logger logger;
 * }</pre>
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectLogger {}
