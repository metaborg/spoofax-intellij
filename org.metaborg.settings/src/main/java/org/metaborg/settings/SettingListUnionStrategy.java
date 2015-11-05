package org.metaborg.settings;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * The result is the union of the new and the previous setting.
 */
/* package private */ final class SettingListUnionStrategy<E> implements ISettingInheritanceStrategy<List<E>> {

    /**
     * {@inheritDoc}
     */
    @Override
    @Nullable
    public List<E> apply(
            final ISettingKey<List<E>> key,
            @Nullable final List<E> localValue,
            @Nullable final List<E> parentValue) {
        if (parentValue == null)
            return localValue;
        if (localValue == null)
            return parentValue;

        List<E> result = new ArrayList<>(parentValue);
        result.addAll(localValue);
        return result;
    }
}
