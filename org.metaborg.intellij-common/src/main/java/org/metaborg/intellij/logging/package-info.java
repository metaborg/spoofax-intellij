/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Logging functionality.
 *
 * Metaborg Core uses an implementation of the {@link org.metaborg.util.log.ILogger}
 * interface, whose default implementation defers logging to SLF4J. IntelliJ IDEA
 * uses an implementation of the {@link com.intellij.openapi.diagnostic.Logger}
 * class, whose default implementation internally also defers logging to SLF4J.
 * Even tough IDEA's use of SLF4J is not visible, it has a dependency on SLF4J, but
 * the versions of SLF4J used by Metaborg Core and IntelliJ IDEA do not match.
 *
 * A logger adapter to make Metaborg Core's use of SLF4J call IntelliJ's logging
 * framework is provided in the {@link org.slf4j.impl.IntelliJLoggerAdapter} class.
 *
 * To get a logger in your classes, use the {@link org.metaborg.intellij.logging.InjectLogger}
 * annotation on a private non-final instance field. For example:
 *
 * <pre>
 * &#064;InjectLogger private ILogger logger;
 * </pre>
 *
 * When a class with the {@link org.metaborg.intellij.logging.InjectLogger} attribute is
 * injected through Guice, its logger is automatically injected as well. When an instance
 * was not created by Guice, its logger will be <code>null</code>. In that case,
 * use {@link org.metaborg.intellij.logging.LoggerUtils2#injectLogger} to inject a logger
 * manually (e.g. {@link org.metaborg.intellij.logging.PrintStreamLogger}.
 * This should only be used in unit tests, where Guice is not available.
 *
 * Injecting loggers {@link org.metaborg.intellij.logging.InjectLogger} is handled by
 * implementations of the {@link com.google.inject.spi.TypeListener} interface, which are
 * bound as listeners in the Guice module. They in turn use an implementation of the
 * {@link com.google.inject.MembersInjector} interface to inject the logger instance.
 *
 * By default IntelliJ IDEA prints only <code>WARN</code> and <code>ERROR</code> log messages.
 * To also print <code>DEBUG</code> and <code>INFO</code> log messages from the Spoofax for
 * IntelliJ IDEA plugin, go to the <code>$IDEA_ROOT$/bin/log.xml</code> file and add the following
 * XML snippet in the root element:
 *
 * <pre>
 * &lt;category name="#org.metaborg.intellij" additivity="false"&gt;
 *   &lt;priority value="DEBUG"/&gt;
 *   &lt;appender-ref ref="CONSOLE-ALL"/&gt;
 *   &lt;appender-ref ref="FILE"/&gt;
 * &lt;/category&gt;
 * </pre>
 */
@NonNullByDefault
package org.metaborg.intellij.logging;

import org.metaborg.intellij.NonNullByDefault;