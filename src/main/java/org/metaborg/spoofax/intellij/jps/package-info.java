/**
 * All classes used solely for the JPS build system live in this package.
 * <p>
 * The JetBrains Project System (JPS) build system is used to build
 * an IntelliJ project. When an IntelliJ project is being build (e.g. by
 * clicking <em>Make Project</em> from the <em>Build</em> menu), a new
 * process is created in which the JPS plugin is loaded.
 * <p>
 * The JPS plugin is registered with the IDEA plugin in the
 * <a href="META-INF.plugin.xml">plugin.xml</a> file under the
 * &lt;compileServer.plugin&gt; tag. All the JPS plugin's dependencies
 * are listed there as well. The JPS plugin exposes services, which are
 * loaded by the build system through the {@link java.util.ServiceLoader} class.
 */
package org.metaborg.spoofax.intellij.jps;