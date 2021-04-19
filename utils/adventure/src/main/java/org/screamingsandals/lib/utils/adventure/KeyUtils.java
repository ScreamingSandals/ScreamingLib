package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.key.Key;
import org.screamingsandals.lib.utils.reflect.Reflect;


@UtilityClass
public class KeyUtils {
    public final Class<?> NATIVE_KEY_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "key", "Key"));

    public Object keyToPlatform(Key key) {
        if (NATIVE_KEY_CLASS.isInstance(key)) {
            return key;
        }
        return keyToPlatform(key, NATIVE_KEY_CLASS);
    }

    public Object keyToPlatform(Key key, Class<?> keyClass) {
        return Reflect
                .getMethod(keyClass, "key", String.class, String.class)
                .invokeStatic(key.namespace(), key.value());
    }

    @SuppressWarnings({"unchecked", "PatternValidation"})
    public Key keyFromPlatform(Object platformObject) {
        if (platformObject instanceof Key) {
            return (Key) platformObject;
        }
        return Key.key(
                Reflect.fastInvokeResulted(platformObject, "namespace").as(String.class),
                Reflect.fastInvokeResulted(platformObject, "value").as(String.class)
        );
    }

}
