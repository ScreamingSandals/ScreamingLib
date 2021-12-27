package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.KeyUtils;

@Data
public final class KeyWrapper implements Wrapper {
    private final Key key;

    @NotNull
    public Key asKey() {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) key.asString();
        } else if (type.isInstance(key)) {
            return (T) key;
        }

        return (T) KeyUtils.keyToPlatform(key, type);
    }
}
