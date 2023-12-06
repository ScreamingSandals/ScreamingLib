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

package org.screamingsandals.lib.packet;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.particle.Particle;
import org.screamingsandals.lib.particle.ParticleData;
import org.screamingsandals.lib.particle.ParticleType;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.math.Vector3Df;
import org.screamingsandals.lib.world.Location;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true, fluent = true)
@Builder(toBuilder = true)
public class ClientboundExplodePacket extends AbstractPacket {
    private final @NotNull Vector3Df location;
    private final float strength;
    private final @NotNull Vector3Df knockBackVelocity;
    @Builder.Default private final @NotNull List<@NotNull Location> blockLocations = List.of();

    @Override
    public void write(@NotNull PacketWriter writer) {
        if (writer.protocol() >= 761) {
            writer.writeVector(location.toVector3D());
        } else {
            writer.writeVector(location);
        }
        writer.writeFloat(strength);
        writer.writeSizedCollection(blockLocations, locationHolder -> writer.writeByteOffset(location, locationHolder.asVectorf()));
        writer.writeVector(knockBackVelocity);

        if (writer.protocol() >= 765) {
            // TODO: make this configurable
            writer.writeVarInt(1);
            writer.writeVarInt(23); // "explosion"
            writer.writeVarInt(22); // "explosion_emitter"
            writer.writeSizedString("minecraft:entity.generic.explode");
            writer.writeBoolean(false);

        }
    }
}
