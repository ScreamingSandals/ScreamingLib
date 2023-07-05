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

package org.screamingsandals.lib.npc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.PlayerMoveEvent;
import org.screamingsandals.lib.hologram.HologramManager;
import org.screamingsandals.lib.npc.event.NPCInteractEvent;
import org.screamingsandals.lib.player.Player;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.utils.annotations.ServiceDependencies;
import org.screamingsandals.lib.visuals.AbstractVisualsManager;
import org.screamingsandals.lib.visuals.LocatableVisual;
import org.screamingsandals.lib.world.Location;

import java.util.Map;
import java.util.UUID;

@Service
@ServiceDependencies(dependsOn = HologramManager.class)
public class NPCManager extends AbstractVisualsManager<NPC> {
    private static @Nullable NPCManager manager;

    public NPCManager() {
        Preconditions.checkArgument(manager == null, "NPCManager has already been initialized!");
        manager = this;
    }

    public static boolean isInitialized() {
        return manager != null;
    }

    public static @NotNull Map<@NotNull UUID, NPC> getActiveNPCS() {
        return Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!").getActiveVisuals();
    }

    @Contract("null -> null")
    public static @Nullable NPC getNPC(@Nullable UUID uuid) {
        Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!");
        return getActiveNPCS().get(uuid);
    }

    public static void addNPC(@NotNull NPC npc) {
        Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!").addVisual(npc.uuid(), npc);
    }

    public static void removeNPC(@Nullable UUID uuid) {
        if (uuid != null) {
            Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!").removeVisual(uuid);
        }
    }

    public static void removeNPC(@NotNull NPC npc) {
        removeNPC(npc.uuid());
    }

    public static @NotNull NPC npc(@NotNull Location holder) {
        var uuid = UUID.randomUUID();

        // According to Grum (Mojang employee), NPCs are supposed to have UUIDs version 2 (randomUUID is supposed to generate version 4)
        // from version 4 to version 2 we can switch easily using the following method: (the following code is from CitizensAPI repository)

        if (uuid.version() == 4) { // switch to version 2
            long msb = uuid.getMostSignificantBits();
            msb &= ~0x0000000000004000L;
            msb |= 0x0000000000002000L;
            uuid = new UUID(msb, uuid.getLeastSignificantBits());
        }

        return npc(uuid, holder, true);
    }

    public static @NotNull NPC npc(@NotNull UUID uuid, @NotNull Location holder, boolean touchable) {
        Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!");
        final var npc = new NPCImpl(uuid, holder, touchable);
        addNPC(npc);
        return npc;
    }

    @OnEvent
    public void onPlayerMove(@NotNull PlayerMoveEvent event) {
        if (activeVisuals.isEmpty()) {
            return;
        }

        final var player = event.player();
        for (final var npc : activeVisuals.values()) {
            if (!npc.shown() || !npc.lookAtPlayer()
                    || !npc.viewers().contains(player) || !player.getLocation().isWorldSame(npc.location())
                    || npc.location().getDistanceSquared(player.getLocation()) > LocatableVisual.DEFAULT_VIEW_DISTANCE) {
                continue;
            }
            npc.lookAtLocation(event.newLocation(), player);
        }
    }

    @Override
    public void fireVisualTouchEvent(@NotNull Player sender, @NotNull NPC visual, @NotNull InteractType interactType) {
        EventManager.fireAsync(new NPCInteractEvent(sender, visual, interactType));
    }
}
