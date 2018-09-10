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

package org.metaborg.intellij.idea.extensions;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageExtensionPoint;

/**
 * Language extension point value wrapper.
 * <p>
 * This wrapper is used to provide an instance to a language extension point instead of a class.
 *
 * @param <T> The type of instance.
 */
public final class InstanceLanguageExtensionPoint<T> extends LanguageExtensionPoint<T> implements IExtensionPointValue {

    private final String id;
    private final T instance;

    /**
     * Initializes a new instance of the {@link InstanceLanguageExtensionPoint} class.
     *
     * @param id The extension point identifier.
     * @param language The language.
     * @param instance The instance.
     */
    public InstanceLanguageExtensionPoint(final String id, final Language language, final T instance) {
        super();
        this.id = id;
        this.instance = instance;
        this.language = language.getID();
        this.implementationClass = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T getInstance() {
        return this.instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return this.id;
    }
}
