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

package org.metaborg.intellij.configuration;

/**
 * Project-level configuration of the plugin.
 */
public interface IMetaborgProjectConfig {

    String CONFIG_NAME = "MetaborgProjectConfig";
    String CONFIG_FILE = "metaborgproject.xml";

    /**
     * Gets the name.
     *
     * @return The name.
     */
    String getName();

    /**
     * Sets the name.
     *
     * @param value The name.
     */
    void setName(String value);

}
