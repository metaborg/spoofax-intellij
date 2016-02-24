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
