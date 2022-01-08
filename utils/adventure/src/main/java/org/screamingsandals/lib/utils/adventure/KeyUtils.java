/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
