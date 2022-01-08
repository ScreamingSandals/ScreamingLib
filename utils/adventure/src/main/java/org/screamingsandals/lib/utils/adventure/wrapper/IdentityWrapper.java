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

    /**
     * {@inheritDoc}
     */
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
