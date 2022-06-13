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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.bukkit.packet.listener.ServerboundInteractPacketListener;
import org.screamingsandals.lib.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.nms.accessors.ArmorStandAccessor;
import org.screamingsandals.lib.nms.accessors.ConnectionAccessor;
import org.screamingsandals.lib.nms.accessors.ServerGamePacketListenerImplAccessor;
import org.screamingsandals.lib.nms.accessors.ServerPlayerAccessor;
import org.screamingsandals.lib.packet.*;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.reflect.Reflect;
import org.screamingsandals.lib.vanilla.packet.PacketIdMapping;

@Service(dependsOn = {
        ServerboundInteractPacketListener.class
})
@RequiredArgsConstructor
public class BukkitPacketMapper extends PacketMapper {

    @Override
    public void sendPacket0(PlayerWrapper player, AbstractPacket packet) {
        Preconditions.checkNotNull(packet, "Packet cannot be null!, skipping packet...");

        final var buffer = Unpooled.buffer();
        try {
            var writer = new CraftBukkitPacketWriter(buffer);
            writer.writeVarInt(packet.getId());

            int dataStartIndex = buffer.writerIndex();
            packet.write(writer);
            if (!writer.isCancelled()) {
                int dataSize = buffer.writerIndex() - dataStartIndex;
                if (dataSize > 2097151) {
                    throw new IllegalArgumentException("Packet too big (is " + dataSize + ", should be less than 2097152): " + packet);
                }

                sendRawPacket(player, buffer);
            }
            writer.getAppendedPackets().forEach(extraPacket -> sendPacket0(player, extraPacket));
        } catch (Throwable t) {
            Bukkit.getLogger().severe("An exception occurred serializing packet of class: " + packet.getClass().getSimpleName());
            t.printStackTrace();
        }
    }

    @Override
    public int getId0(Class<? extends AbstractPacket> clazz) {
        var id = PacketIdMapping.getPacketId(clazz);
        return id == null ? -1 : id; // peacefully return some number, it will be ignored later
    }

    @Override
    public int getArmorStandTypeId0() {
        return ClassStorage.getEntityTypeId("armor_stand", ArmorStandAccessor.getType());
    }

    // TODO: Optimize: usage of write() instead and flushing manually at the end for multiple writes, also expose this method later on
    protected void sendRawPacket(PlayerWrapper player, ByteBuf buffer) {
        var channel = getChannel(player);
        if (channel.isActive()) {
            final var ctx = channel.pipeline().context("encoder");
            if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) { // not rly cacheable, reloads exist, soft-depend is sus
                // ViaVersion fixes incompatibilities with other plugins, so we just use it if it's present
                final var conn = Via.getAPI().getConnection(player.getUuid());
                if (conn != null) {
                    try {
                        conn.transformClientbound(buffer, CancelEncoderException::generate);
                    } catch (Throwable ignored) {
                        // no u Via
                    }
                }

                // TODO: ProtocolSupport
            }

            final Runnable task = () -> {
                if (ctx != null) {
                    ctx.writeAndFlush(buffer);
                } else {
                    channel.writeAndFlush(buffer);
                }
            };

            if (channel.eventLoop().inEventLoop()) {
                task.run();
            } else {
                channel.eventLoop().submit(task);
            }
        }
    }

    public Channel getChannel(PlayerWrapper player) {
        return (Channel) Reflect.getFieldResulted(ClassStorage.getHandle(player.raw()), ServerPlayerAccessor.getFieldConnection())
                .getFieldResulted(ServerGamePacketListenerImplAccessor.getFieldConnection())
                .getFieldResulted(ConnectionAccessor.getFieldChannel())
                .raw();
    }
}
