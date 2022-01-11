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

package org.screamingsandals.lib.bukkit.packet;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.exception.CancelEncoderException;
import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.bukkit.packet.listener.ServerboundInteractPacketListener;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ArmorStandAccessor;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.logger.LoggerWrapper;
import org.screamingsandals.lib.vanilla.packet.PacketIdMapping;

@Service(initAnother = {
        ServerboundInteractPacketListener.class
})
@RequiredArgsConstructor
public class BukkitPacketMapper extends PacketMapper {
    private final LoggerWrapper logger;
    private boolean viaEnabled;

    @OnPostEnable
    public void onPostEnable() {
        viaEnabled = Bukkit.getPluginManager().isPluginEnabled("ViaVersion");
    }

    @Override
    public void sendPacket0(PlayerWrapper player, AbstractPacket packet) {
        Preconditions.checkNotNull(packet, "Cannot send null packet to player!");
        Preconditions.checkNotNull(player, "Cannot send packet to null player!");

        if (!player.isOnline()) {
            logger.trace("Player: {} is offline, ignoring packet: {}", player.getName(), packet.getClass().getSimpleName());
            return;
        }

        final var buffer = Unpooled.buffer();
        try {
            var writer = new CraftBukkitPacketWriter(buffer);
            writer.writeVarInt(packet.getId());

            int dataStartIndex = writer.getBuffer().writerIndex();
            packet.write(writer);

            int dataSize = writer.getBuffer().writerIndex() - dataStartIndex;
            if (dataSize > 2097151) {
                throw new IllegalArgumentException("Packet too big (is " + dataSize + ", should be less than 2097152): " + packet);
            }

            var channel = player.getChannel();
            if (channel.isActive()) {
                if (viaEnabled) {
                    final var conn = Via.getAPI().getConnection(player.getUuid());
                    if (conn != null) {
                        conn.transformClientbound(writer.getBuffer(), CancelEncoderException::generate);
                    } else {
                        channel.eventLoop().execute(() -> channel.writeAndFlush(writer.getBuffer()));
                    }
                } else {
                    channel.eventLoop().execute(() -> channel.writeAndFlush(writer.getBuffer()));
                }
            }

            writer.getAppendedPackets().forEach(extraPacket -> sendPacket0(player, extraPacket));
        } catch (Throwable t) {
            buffer.release();
            Bukkit.getLogger().severe("An exception occurred serializing packet of class: " + packet.getClass().getSimpleName() + " for player: " + player.getName());
            t.printStackTrace();
        }
    }

    @Override
    public int getId0(Class<? extends AbstractPacket> clazz) {
        return PacketIdMapping.getPacketId(clazz);
    }

    @Override
    public int getArmorStandTypeId0() {
        return ClassStorage.getEntityTypeId("armor_stand", ArmorStandAccessor.getType());
    }
}
