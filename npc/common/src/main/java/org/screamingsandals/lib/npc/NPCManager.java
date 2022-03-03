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

package org.screamingsandals.lib.npc;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerMoveEvent;
import org.screamingsandals.lib.npc.event.NPCInteractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.plugin.PluginManager;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.utils.PlatformType;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.visuals.AbstractVisualsManager;
import org.screamingsandals.lib.visuals.LocatableVisual;
import org.screamingsandals.lib.world.LocationHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class NPCManager extends AbstractVisualsManager<NPC> {
    private static NPCManager manager = null;
    private static boolean IS_BUNGEECORD_ENABLED;

    public NPCManager(Path dataFolder) {
        Preconditions.checkArgument(manager == null, "NPCManager has already been initialized!");
        manager = this;

        File spigotYmlFile = dataFolder.getParent().resolveSibling("spigot.yml").toFile();
        IS_BUNGEECORD_ENABLED = false;
        Pattern pattern = Pattern.compile("bungeecord: ?(?:y|yes|true|on)", Pattern.CASE_INSENSITIVE);

        if (PluginManager.getPlatformType() == PlatformType.BUKKIT && spigotYmlFile.isFile()) {
            try (Scanner scanner = new Scanner(spigotYmlFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine().toLowerCase();

                    if (pattern.matcher(line).matches())
                        IS_BUNGEECORD_ENABLED = true;
                }
            } catch (FileNotFoundException e) {
                //Will never happen, we checked spigotYmlFile.isFile()
            }
        }
    }
    
    
        
    

    public static boolean isBungeecordEnabled() {
        return IS_BUNGEECORD_ENABLED;
    }
    
    public static boolean isInitialized() {
        return manager != null;
    }

    public static Map<UUID, NPC> getActiveNPCS() {
        return Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!").getActiveVisuals();
    }

    public static Optional<NPC> getNPC(UUID uuid) {
        Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!");
        return Optional.ofNullable(getActiveNPCS().get(uuid));
    }

    public static void addNPC(NPC npc) {
        Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!").addVisual(npc.uuid(), npc);
    }

    public static void removeNPC(UUID uuid) {
        getNPC(uuid).ifPresent(NPCManager::removeNPC);
    }

    public static void removeNPC(NPC npc) {
        Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!").removeVisual(npc.uuid());
    }

    public static NPC npc(LocationHolder holder) {
        return npc(UUID.randomUUID(), holder, true);
    }

    public static NPC npc(UUID uuid, LocationHolder holder, boolean touchable) {
        Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!");
        final var npc = new NPCImpl(uuid, holder, touchable);
        addNPC(npc);
        return npc;
    }

    @OnEvent
    public void onPlayerMove(SPlayerMoveEvent event) {
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
    public void fireVisualTouchEvent(PlayerWrapper sender, NPC visual, InteractType interactType) {
        EventManager.fireAsync(new NPCInteractEvent(sender, visual, interactType));
    }
}
