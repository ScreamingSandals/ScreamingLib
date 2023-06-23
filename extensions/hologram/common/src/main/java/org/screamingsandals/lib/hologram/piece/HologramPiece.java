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

package org.screamingsandals.lib.hologram.piece;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.ClientboundSetEntityDataPacket;
import org.screamingsandals.lib.packet.ClientboundTeleportEntityPacket;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.world.Location;

import java.util.List;

public interface HologramPiece {
    int getId();

    void setText(@NotNull Component text, @Nullable AudienceComponentLike audienceComponentLike);

    @Nullable Component getText();

    @Nullable AudienceComponentLike getAudienceComponent();

    @NotNull ClientboundSetEntityDataPacket getMetadataPacket();

    @NotNull ClientboundSetEntityDataPacket getMetadataPacket(@NotNull Player viewer);

    @NotNull ClientboundTeleportEntityPacket getTeleportPacket();

    @NotNull List<@NotNull AbstractPacket> getSpawnPackets();

    @NotNull List<@NotNull AbstractPacket> getSpawnPackets(@NotNull Player viewer);

    void setLocation(@NotNull Location location);
}
