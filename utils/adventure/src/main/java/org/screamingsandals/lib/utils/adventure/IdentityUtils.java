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
import net.kyori.adventure.identity.Identity;
import org.screamingsandals.lib.utils.reflect.Reflect;

import java.util.UUID;

@UtilityClass
public class IdentityUtils {
    public final Class<?> NATIVE_IDENTITY_CLASS
            = Reflect.getClassSafe(String.join(".", "net", "kyori", "adventure", "identity", "Identity"));

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
