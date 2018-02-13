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

package org.metaborg.intellij.idea.languages;

import com.google.inject.Inject;
import com.intellij.lang.Language;
import org.metaborg.core.language.*;
import org.metaborg.intellij.idea.SpoofaxIdeaPlugin;

///**
// * A Metaborg language used in IntelliJ IDEA.
// * <p>
// * There are no implementations of this class because it's instantiated dynamically.
// */
//public abstract class SpoofaxIdeaLanguage extends Language {
//
////    private ILanguage language;
////    @InjectLogger
////    private ILogger logger;
//
//    /**
//     * This instance is created by the proxy system.
//     * Do not call this constructor manually.
//     *
//     * @param language The language.
//     */
//    protected SpoofaxIdeaLanguage(final ILanguage language) {
//        super(language.name());
//        SpoofaxIdeaPlugin.injector().injectMembers(this);
//    }
//
//    @Inject
//    @SuppressWarnings("unused")
//    private void inject() {
//    }
//
//
//    //    // TODO: If possible, remove these getters/setters.
////    // Instead, a Spoofax language is identified by its name.
////    /**
////     * Gets the associated language.
////     *
////     * @return The associated language.
////     */
////    private ILanguage getLanguage() {
////        return this.language;
////    }
////
////    /**
////     * Sets the associated language.
////     *
////     * The newly associated language must have the same language name.
////     *
////     * @param language The associated language.
////     */
////    private void setLanguage(final ILanguage language) {
////        if (!language.name().equals(this.getID())) throw new IllegalArgumentException(this.logger.format(
////                "The expected language name {} does not match the actual language name {}.",
////                this.getID(),
////                language.name())
////        );
////        this.language = language;
////    }
//
//    // The parent toString() is sufficient.
////    @Override
////    public String toString() {
////        return getLanguage().toString();
////    }
//}
