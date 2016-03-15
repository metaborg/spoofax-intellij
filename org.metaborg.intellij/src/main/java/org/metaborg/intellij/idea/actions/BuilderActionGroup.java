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

package org.metaborg.intellij.idea.actions;

import com.google.inject.*;
import com.google.inject.assistedinject.*;
import com.intellij.openapi.actionSystem.*;
import org.metaborg.core.language.*;

/**
 * Action group for a language builder.
 */
public final class BuilderActionGroup extends DefaultActionGroup {

    private final ILanguageImpl languageImpl;
    private final ActionUtils actionUtils;

    /**
     * Initializes a new instance of the {@link BuilderActionGroup} class.
     *
     * @param languageImpl The language implementation to respond to.
     */
    @Inject
    public BuilderActionGroup(
            @Assisted final ILanguageImpl languageImpl,
            final ActionUtils actionUtils) {
        super(getName(languageImpl), true);
        this.languageImpl = languageImpl;
        this.actionUtils = actionUtils;
    }

    private static String getName(final ILanguageImpl languageImpl) {
        return languageImpl.belongsTo().name();
    }

    @Override
    public void update(final AnActionEvent e) {
        final boolean visible = this.actionUtils.isActiveFileLanguage(e, this.languageImpl);
        e.getPresentation().setVisible(visible);
        super.update(e);
    }

}
