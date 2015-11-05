package org.metaborg.settings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The result is the union of the new and the previous setting.
 */
/* package private */ final class SettingCollectionUnionStrategy<E> implements ISettingInheritanceStrategy<Collection<E>> {

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public Collection<E> apply(
            final ISettingKey<Collection<E>> key,
            @Nullable final Collection<E> localValue,
            @Nullable final Collection<E> parentValue) {
        if (parentValue == null)
            return localValue;
        if (localValue == null)
            return parentValue;

        List<E> result = new ArrayList<>(parentValue);
        result.addAll(localValue);
        return result;
    }
}
