package org.metaborg.settings;

import javax.annotation.Nullable;

/**
 * Specifies how a setting should be merged with its parent setting.
 */
public interface ISettingInheritanceStrategy<T> {

    /**
     * Applies the inheritance strategy to the specified values.
     *
     * Note that when this method is invoked, <em>both</em> the local and the
     * parent value were set (even though one or both may be <code>null</code>).
     *
     * @param key The setting key.
     * @param localValue The local value, which may be <code>null</code>.
     * @param parentValue The parent value, which may be <code>null</code>.
     * @return The resulting value, which may be <code>null</code>.
     */
    @Nullable
    T apply(ISettingKey<T> key, @Nullable T localValue, @Nullable T parentValue);

}
