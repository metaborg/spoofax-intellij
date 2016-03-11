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

package org.metaborg.intellij.idea.sdks;

import com.intellij.openapi.options.*;
import com.intellij.openapi.projectRoots.*;
import org.jdom.*;

import javax.annotation.*;

public final class MetaborgSdkAdditionalData implements ValidatableSdkAdditionalData {

    private static final String JDK_NAME = "jdkName";

    private final Sdk sdk;
    @Nullable private Sdk jdk;
    @Nullable private String jdkName;

    /**
     * Initializes a new instance of the {@link MetaborgSdkAdditionalData} class.
     *
     * @param sdk The Metaborg SDK.
     * @param jdk The underlying JDK.
     */
    public MetaborgSdkAdditionalData(final Sdk sdk, @Nullable final Sdk jdk) {
        this.sdk = sdk;
        this.jdk = jdk;
        this.jdkName = null;
    }

    /**
     * Initializes a new instance of the {@link MetaborgSdkAdditionalData} class.
     *
     * @param sdk The Metaborg SDK.
     * @param jdkName The JDK name; or <code>null</code>.
     */
    private MetaborgSdkAdditionalData(final Sdk sdk, @Nullable final String jdkName) {
        this.sdk = sdk;
        this.jdk = null;
        this.jdkName = jdkName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkValid(final SdkModel sdkModel)
            throws ConfigurationException {
        if (getJdk() == null) {
            throw new ConfigurationException("Please configure a JDK.");
        }
    }

    /**
     * Loads the additional data from the specified element.
     *
     * @param element The persisted element.
     * @param sdk The Metaborg SDK.
     * @return The additional data.
     */
    public static MetaborgSdkAdditionalData load(final Element element, final Sdk sdk) {
        final String jdkName = element.getAttributeValue(JDK_NAME);
        return new MetaborgSdkAdditionalData(sdk, jdkName);
    }

    /**
     * Saves the additional data to the specified element.
     *
     * @param element The persisted element.
     */
    public void save(final Element element) {
        @Nullable final Sdk jdk = getJdk();
        if (jdk == null) return;

        element.setAttribute(JDK_NAME, jdk.getName());
    }

    /**
     * Gets the JDK.
     *
     * @return The JDK; or <code>null</code>.
     */
    @Nullable
    public Sdk getJdk() {
        if (this.jdk != null) return this.jdk;

        @Nullable final Sdk jdk;
        if (this.jdkName != null) {
            // After the SDK data is deserialized, we only have the JDK's name.
            // Let's find the JDK with that name.
            jdk = ProjectJdkTable.getInstance().findJdk(this.jdkName);
        } else {
            jdk = getAnAcceptableJdk();
        }

        setJdk(jdk);
        return jdk;
    }

    /**
     * Sets the JDK.
     *
     * @param jdk The JDK.
     */
    public void setJdk(@Nullable final Sdk jdk) {
        this.jdk = jdk;
        this.jdkName = null;
    }

    /**
     * Gets an acceptable JDK.
     *
     * @return The JDK; or <code>null</code>.
     */
    @Nullable
    private Sdk getAnAcceptableJdk() {
        final ProjectJdkTable jdkTable = ProjectJdkTable.getInstance();
        for (final Sdk jdk : jdkTable.getAllJdks()) {
            if (MetaborgSdkType.isAcceptableJdk(jdk, this.sdk)) {
                return jdk;
            }
        }
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new MetaborgSdkAdditionalData(this.sdk, this.jdk);
    }
}
