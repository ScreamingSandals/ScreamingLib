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
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.packet.entity.FakeArmorStandEntity;
import org.screamingsandals.lib.spectator.AudienceComponentLike;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.world.Location;

public final class ArmorStandHologramPiece extends FakeArmorStandEntity implements HologramPiece {
    public static final int ENTITY_TYPE_ID = PacketMapper.getArmorStandTypeId();

    public ArmorStandHologramPiece(@NotNull Location location) {
        super(location, ENTITY_TYPE_ID);
    }

    @Override
    public void setText(@NotNull Component text, @Nullable AudienceComponentLike audienceComponentLike) {
        setCustomName(text);
        setCustomNameSenderMessage(audienceComponentLike);
    }

    @Override
    public @NotNull Component getText() {
        return getCustomName();
    }

    @Override
    public @Nullable AudienceComponentLike getAudienceComponent() {
        return getCustomNameSenderMessage();
    }
}
