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

package org.screamingsandals.lib.impl.bukkit.packet;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.exception.CancelEncoderException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.impl.bukkit.packet.listener.ServerboundInteractPacketListener;
import org.screamingsandals.lib.impl.bukkit.utils.nms.ClassStorage;
import org.screamingsandals.lib.impl.nms.accessors.ArmorStandAccessor;
import org.screamingsandals.lib.packet.AbstractPacket;
import org.screamingsandals.lib.packet.PacketMapper;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.internal.AccessPluginClasses;
import org.screamingsandals.lib.impl.vanilla.packet.PacketIdMapping;

@Service
@ServiceDependencies(dependsOn = ServerboundInteractPacketListener.class)
@RequiredArgsConstructor
@AccessPluginClasses({"ViaVersion", "ProtocolSupport"})
public class BukkitPacketMapper extends PacketMapper {

    @Override
    public void sendPacket0(@NotNull Player player, @NotNull AbstractPacket packet) {
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
            buffer.release();
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

    @Override
    public int getTextDisplayTypeId0() {
        return ClassStorage.getEntityTypeId("text_display", null); // second argument is irrelevant for 1.19.4+
    }

    // TODO: Optimize: usage of write() instead and flushing manually at the end for multiple writes, also expose this method later on
    protected void sendRawPacket(Player player, ByteBuf buffer) {
        var channel = Players.getNettyChannel(player);
        if (channel.isActive()) {
            var ctx = channel.pipeline().context("encoder");
            if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) { // not rly cacheable, reloads exist, soft-depend is sus
                var viaCtx = channel.pipeline().context("via-encoder");
                if (viaCtx != null) {
                    ctx = viaCtx;
                }
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

            var finalCtx = ctx;
            final Runnable task = () -> {
                if (finalCtx != null) {
                    finalCtx.writeAndFlush(buffer);
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
}
