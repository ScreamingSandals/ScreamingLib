package org.screamingsandals.lib.utils.adventure.wrapper;

import lombok.Data;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.adventure.IdentityUtils;

import java.util.UUID;

@Data
public final class IdentityWrapper implements Wrapper {
    private final Identity identity;

    @NotNull
    public Identity asIdentity() {
        return identity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T as(Class<T> type) {
        if (String.class.isAssignableFrom(type)) {
            return (T) identity.uuid().toString();
        } else if (UUID.class.isAssignableFrom(type)) {
            return (T) identity.uuid();
        } else if (type.isInstance(identity)) {
            return (T) identity;
        }

        return (T) IdentityUtils.identityToPlatform(identity, type);
    }
}
