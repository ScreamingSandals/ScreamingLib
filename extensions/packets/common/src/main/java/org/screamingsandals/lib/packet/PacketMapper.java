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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.annotations.AbstractService;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;

import java.util.Collection;

/**
 *  Represents the PacketMapper instance which is useful for sending {@link AbstractPacket} to Player connections.
 */
@AbstractService("org.screamingsandals.lib.impl.{platform}.packet.{Platform}PacketMapper")
@ServiceDependencies(dependsOn = ProtocolInjector.class)
public abstract class PacketMapper {
    private static @Nullable PacketMapper packetMapper;

    public PacketMapper() {
        if (packetMapper != null) {
            throw new UnsupportedOperationException("PacketMapper is already initialized.");
        }
        packetMapper = this;
    }

    /**
     * Returns if this PacketMapper instance has been initialized.
     *
     * @return true if the PacketMapper instance has been initialized, false otherwise
     */
    public static boolean isInitialized() {
        return packetMapper != null;
    }

    /**
     * Sends the packet to the client the player is currently connected with.
     *
     * @param player the player to send the packet to
     * @param packet the packet instance to send the player
     */
    public static void sendPacket(@NotNull Player player, @NotNull AbstractPacket packet) {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        packetMapper.sendPacket0(player, packet);
    }

    /**
     * Platform specific method that sends the packet to the client the player is currently connected with.
     *
     * @param player the player to send the packet to
     * @param packet the packet instance to send the player
     */
    public abstract void sendPacket0(@NotNull Player player, @NotNull AbstractPacket packet);


    /**
     * Sends the packet to the clients of all the players that are connected with.
     *
     * @param players the players to send the packet to
     * @param packet the packet instance to send the players
     */
    public static void sendPacket(@NotNull Collection<@NotNull Player> players, @NotNull AbstractPacket packet) {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        players.forEach(player -> packetMapper.sendPacket0(player, packet));
    }

    public static int getId(@NotNull Class<? extends AbstractPacket> clazz) {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        return packetMapper.getId0(clazz);
    }

    public static int getArmorStandTypeId() {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        return packetMapper.getArmorStandTypeId0();
    }

    public static int getTextDisplayTypeId() {
        if (packetMapper == null) {
            throw new UnsupportedOperationException("PacketMapper isn't initialized yet.");
        }
        return packetMapper.getTextDisplayTypeId0();
    }

    public abstract int getId0(@NotNull Class<? extends AbstractPacket> clazz);

    public abstract int getArmorStandTypeId0();

    public abstract int getTextDisplayTypeId0();
}
