package org.metaborg.settings;

import javax.annotation.Nullable;

/**
 * The local setting overrides the previous setting,
 * even when the local setting is <code>null</code>.
 */
/* package private */ final class SettingOverrideStrategy<T> implements ISettingInheritanceStrategy<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public T apply(final ISettingKey<T> key, @Nullable final T currentValue, @Nullable final T parentValue) {
        return currentValue;
    }
}
