/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.npc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.npc.skin.NPCSkin;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.visuals.TouchableVisual;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.packet.ClientboundSetPlayerTeamPacket;

import java.util.List;

/**
 * Represents the API for NPCs.
 */
public interface NPC extends TouchableVisual<NPC> {

    /**
     * Creates a new NPC.
     *
     * @param location the location of the newly created NPC
     * @return the created NPC
     */
    @Contract(value = "_ -> new", pure = true)
    static @NotNull NPC of(@NotNull Location location) {
        return NPCManager.npc(location);
    }

    /**
     * Gets the display name text entries of this NPC.
     *
     * @return a list containing text entries for the NPC name, null if not set
     * @see NPC#displayName(List) for setting the display name
     */
    @Unmodifiable @Nullable List<@NotNull TextEntry> displayName();

    /**
     * Gets the skin of this NPC.
     *
     * @return the skin of this NPC
     * @see NPC#skin(NPCSkin) for setting the skin of this NPC
     */
    @NotNull NPCSkin skin();

    /**
     * Sets the display name text entries of this NPC.
     *
     * @param name name (multi-lined) of the NPC.
     * @return this NPC
     * @see NPC#displayName() for getting the current display name
     */
    @NotNull NPC displayName(@NotNull List<@NotNull Component> name);

    /**
     * Gets the ID of this NPC entity.
     *
     * @return the ID of this NPC entity
     */
    int entityId();

    /**
     * Sets the skin of this NPC.
     *
     * @param skin the skin
     * @return this NPC
     * @see NPC#skin() for getting the current skin of this NPC
     */
    @NotNull NPC skin(@Nullable NPCSkin skin);

    /**
     * Makes the NPC look at the specified location in the eyes of the specified player.
     *
     * @param location the location
     */
    void lookAtLocation(@NotNull Location location, @NotNull Player player);

    /**
     * Gets the entity name.
     *
     * @return a single line name of the entity generated from the UUID
     */
    @NotNull Component tabListName();

    /**
     * Sets whether the line of sight of this NPC should follow a viewer.
     *
     * @param shouldLook should the NPC rotate its head towards a viewer?
     * @return this NPC
     * @see NPC#lookAtPlayer() for determining the current state
     */
    @NotNull NPC lookAtPlayer(boolean shouldLook);

    /**
     * Determines if the line of sight of this NPC is following a viewer.
     *
     * @return is the NPC rotating its head towards the viewer?
     * @see NPC#lookAtPlayer(boolean) for setting the current state
     */
    boolean lookAtPlayer();

    /**
     * Gets the {@link Hologram} instance that displays the name of this NPC.
     *
     * @return the hologram
     */
    @NotNull Hologram hologram();

    /**
     * Determines the level of elevation of the hologram that displays the name-tag of the NPC above its head.
     *
     * @param hologramElevation amount of elevation in block size approximation, example 1 -> height of 1 block
     * @return the hologram
     */
    @NotNull NPC hologramElevation(double hologramElevation);

    ClientboundSetPlayerTeamPacket.@NotNull CollisionRule collisionRule();

    @NotNull NPC collisionRule(ClientboundSetPlayerTeamPacket.@NotNull CollisionRule collisionRule);
}
