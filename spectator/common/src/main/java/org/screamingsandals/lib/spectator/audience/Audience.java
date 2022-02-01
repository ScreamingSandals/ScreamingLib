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

package org.screamingsandals.lib.spectator.audience;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.adapter.Adapter;
import org.screamingsandals.lib.utils.UniqueIdentifiable;

import java.util.UUID;

public interface Audience {

    default void sendMessage(@NotNull ComponentLike message) {
        sendMessage((UUID) null, message, MessageType.CHAT);
    }

    default void sendMessage(@NotNull ComponentLike message, @NotNull MessageType messageType) {
        sendMessage((UUID) null, message, messageType);
    }

    default void sendMessage(@Nullable UniqueIdentifiable identifiable, @NotNull ComponentLike message) {
        sendMessage(identifiable != null ? identifiable.uuid() : null, message, MessageType.CHAT);
    }

    default void sendMessage(@Nullable UniqueIdentifiable identifiable, @NotNull ComponentLike message, @NotNull MessageType messageType) {
        sendMessage(identifiable != null ? identifiable.uuid() : null, message, messageType);
    }

    default void sendMessage(@Nullable UUID source, @NotNull ComponentLike message) {
        sendMessage(source, message, MessageType.CHAT);
    }

    void sendMessage(@Nullable UUID source, @NotNull ComponentLike message, @NotNull MessageType messageType);

    interface ForwardingToMulti extends Audience {
        @NotNull
        @ApiStatus.OverrideOnly
        Iterable<? extends Audience> audiences();

        default void sendMessage(@Nullable UUID source, @NotNull ComponentLike message, @NotNull MessageType messageType) {
            audiences().forEach(audience -> sendMessage(source, message, messageType));
        }
    }

    interface ForwardingToSingle extends Audience {
        @NotNull
        @ApiStatus.OverrideOnly
        Audience audience();

        default void sendMessage(@Nullable UUID source, @NotNull ComponentLike message, @NotNull MessageType messageType) {
            audience().sendMessage(source, message, messageType);
        }
    }

    @ApiStatus.Internal
    interface ForwardingToAdapter extends Audience {
        @NotNull
        @ApiStatus.OverrideOnly
        Adapter adapter();

        default void sendMessage(@Nullable UUID source, @NotNull ComponentLike message, @NotNull MessageType messageType) {
            adapter().sendMessage(source, message, messageType);
        }
    }
}
