package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.identity.Identity;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.UUID;

@UtilityClass
public class IdentityUtils {
    public final Class<?> NATIVE_IDENTITY_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.identity.Identity");

    public Object identityToPlatform(Identity identity) {
        if (NATIVE_IDENTITY_CLASS.isInstance(identity)) {
            return identity;
        }
        return identityToPlatform(identity, NATIVE_IDENTITY_CLASS);
    }

    public Object identityToPlatform(Identity identity, Class<?> identityClass) {
        return Reflect
                .getMethod(identityClass, "identity", UUID.class)
                .invokeStatic(identity.uuid());
    }

    @SuppressWarnings("unchecked")
    public Identity identityFromPlatform(Object platformObject) {
        if (platformObject instanceof Identity) {
            return (Identity) platformObject;
        }
        return Identity.identity(Reflect.fastInvokeResulted(platformObject, "uuid").as(UUID.class));
    }
}
