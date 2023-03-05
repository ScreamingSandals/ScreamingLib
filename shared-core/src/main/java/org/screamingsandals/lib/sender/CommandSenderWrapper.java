/*
 * Copyright 2023 ScreamingSandals
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

package org.screamingsandals.lib.sender;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.sender.permissions.Permission;
import org.screamingsandals.lib.sender.permissions.SimplePermission;
import org.screamingsandals.lib.spectator.audience.Audience;
import org.screamingsandals.lib.api.Wrapper;

import java.util.Locale;

public interface CommandSenderWrapper extends Wrapper, Operator, Audience.ForwardingToAdapter {

    @NotNull Type getType();
    default boolean hasPermission(@NotNull String permission) {
        return hasPermission(SimplePermission.of(permission));
    }

    boolean hasPermission(@NotNull Permission permission);

    default boolean isPermissionSet(@NotNull String permission) {
        return isPermissionSet(SimplePermission.of(permission));
    }

    boolean isPermissionSet(@NotNull Permission permission);

    @NotNull String getName();

    @NotNull Locale getLocale();

    enum Type {
        PLAYER,
        CONSOLE,
        UNKNOWN
    }
}
