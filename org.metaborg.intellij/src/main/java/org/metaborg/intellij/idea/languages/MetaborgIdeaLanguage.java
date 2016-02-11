/*
 * Copyright © 2015-2016
 *
 * This file is part of Spoofax for IntelliJ.
 *
 * Spoofax for IntelliJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoofax for IntelliJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Spoofax for IntelliJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.metaborg.intellij.idea.languages;

import com.google.inject.*;
import com.intellij.lang.Language;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.*;
import org.metaborg.intellij.idea.graphics.*;
import org.metaborg.intellij.logging.*;
import org.metaborg.util.log.*;

/**
 * A Metaborg language used in IntelliJ IDEA.
 * <p>
 * There are no implementations of this class because it's instantiated dynamically.
 */
public abstract class MetaborgIdeaLanguage extends Language {

//    private ILanguage language;
//    @InjectLogger
//    private ILogger logger;

    /**
     * This instance is created by the proxy system.
     * Do not call this constructor manually.
     *
     * @param language The language.
     */
    protected MetaborgIdeaLanguage(final ILanguage language) {
        super(language.name());
        SpoofaxIdeaPlugin.injector().injectMembers(this);
    }

    @Inject
    @SuppressWarnings("unused")
    private void inject() {
    }

//    // TODO: If possible, remove these getters/setters.
//    // Instead, a Spoofax language is identified by its name.
//    /**
//     * Gets the associated language.
//     *
//     * @return The associated language.
//     */
//    private ILanguage getLanguage() {
//        return this.language;
//    }
//
//    /**
//     * Sets the associated language.
//     *
//     * The newly associated language must have the same language name.
//     *
//     * @param language The associated language.
//     */
//    private void setLanguage(final ILanguage language) {
//        if (!language.name().equals(this.getID())) throw new IllegalArgumentException(this.logger.format(
//                "The expected language name {} does not match the actual language name {}.",
//                this.getID(),
//                language.name())
//        );
//        this.language = language;
//    }

    // The parent toString() is sufficient.
//    @Override
//    public String toString() {
//        return getLanguage().toString();
//    }
}
