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

package org.screamingsandals.lib.packet.entity;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.packet.ClientboundSetEntityDataPacket;
import org.screamingsandals.lib.packet.MetadataItem;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.world.Location;

import java.util.List;

@Getter
public class FakeTextDisplayEntity extends FakeDisplayEntity {
    @Getter
    private @Nullable Component text;
    @Getter
    @Setter
    private @Nullable AudienceComponentLike textSenderMessage;

    public FakeTextDisplayEntity(@NotNull Location location, int typeId) {
        super(location, typeId);
    }

    public void setText(@NotNull Component component) {
        text = component;
        put(MetadataItem.of((byte) 22, component));
    }

    @Override
    public @NotNull ClientboundSetEntityDataPacket getMetadataPacket(@NotNull Player viewer) {
        if (textSenderMessage != null) {
            return getMetadataPacket(List.of(MetadataItem.of((byte) 22, textSenderMessage.asComponent(viewer))));
        }
        return getMetadataPacket(List.of());
    }
}
