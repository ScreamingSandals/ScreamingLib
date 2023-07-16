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

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.Server;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.EventExecutionOrder;
import org.screamingsandals.lib.event.player.PlayerJoinEvent;
import org.screamingsandals.lib.event.player.PlayerLeaveEvent;
import org.screamingsandals.lib.event.player.PlayerLoginEvent;
import org.screamingsandals.lib.packet.event.SPacketEvent;
import org.screamingsandals.lib.player.Players;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.PacketMethod;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.utils.annotations.methods.OnPostEnable;
import org.screamingsandals.lib.utils.annotations.methods.OnPreDisable;

@Service
@ServiceDependencies(dependsOn = {
        EventManager.class,
        Players.class,
})
public class ProtocolInjector {
    private static final @NotNull String CHANNEL_NAME = "SPacketHandler";

    @OnPostEnable
    public void onPostEnable() {
        if (EventManager.getDefaultEventManager() == null) {
            throw new UnsupportedOperationException("Default EventManager is not initialized yet");
        }

        EventManager.getDefaultEventManager().register(PlayerLoginEvent.class, sPlayerLoginEvent -> addPlayer(sPlayerLoginEvent.player(), true), EventExecutionOrder.FIRST);
        EventManager.getDefaultEventManager().register(PlayerJoinEvent.class, sPlayerJoinEvent -> addPlayer(sPlayerJoinEvent.player(), false), EventExecutionOrder.LATE);
        EventManager.getDefaultEventManager().register(PlayerLeaveEvent.class, sPlayerLeaveEvent -> removePlayer(sPlayerLeaveEvent.player()), EventExecutionOrder.LAST);
        Server.getConnectedPlayers().forEach(player -> addPlayer(player, false));
    }

    @OnPreDisable
    public void onPreDisable() {
        Server.getConnectedPlayers().forEach(this::removePlayer);
    }

    public void addPlayer(@NotNull Player player, boolean onLogin) {
        try {
            final var channel = Players.getNettyChannel(player);
            Preconditions.checkNotNull(channel, "Failed to find player channel!");

            final var handler = new PacketHandler(player);
            if (channel.pipeline().get(CHANNEL_NAME) == null && channel.pipeline().get("packet_handler") != null) {
                channel.eventLoop()
                        .submit(() -> channel.pipeline().addBefore("packet_handler", CHANNEL_NAME, handler));
            }
        } catch (Throwable t) {
            if (onLogin) {
                // could be possible we did this a bit too early, SPlayerJoinEvent will take care of this now.
                return;
            }
            t.printStackTrace();
            player.kick(Component.text("Failed to inject!, please rejoin.."));
        }
    }

    public void removePlayer(@NotNull Player player) {
        try {
            final var channel = Players.getNettyChannel(player);
            if (channel != null && channel.pipeline().get(CHANNEL_NAME) != null) {
                channel.eventLoop()
                        .submit(() -> channel.pipeline().remove(CHANNEL_NAME));
            }
        } catch (Throwable ignored) {
        }
    }

    @RequiredArgsConstructor
    private static class PacketHandler extends ChannelDuplexHandler {
        private final @NotNull Player player;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
            final var readEvent = EventManager.fire(new SPacketEvent(player, PacketMethod.INBOUND, packet));
            if (readEvent.isCancelled()) {
                return;
            }

            final var modifiedPacket = readEvent.getPacket();
            super.channelRead(ctx, modifiedPacket);
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
            final var writeEvent = EventManager.fire(new SPacketEvent(player, PacketMethod.OUTBOUND, packet));
            if (writeEvent.cancelled()) {
                return;
            }

            final var modifiedPacket = writeEvent.getPacket();
            super.write(ctx, modifiedPacket, promise);
        }
    }
}
