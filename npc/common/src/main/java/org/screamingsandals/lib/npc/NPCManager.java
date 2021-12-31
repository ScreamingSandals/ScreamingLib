package org.screamingsandals.lib.npc;

import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.OnEvent;
import org.screamingsandals.lib.event.player.SPlayerMoveEvent;
import org.screamingsandals.lib.npc.event.NPCInteractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.utils.Preconditions;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.visuals.AbstractVisualsManager;
import org.screamingsandals.lib.visuals.LocatableVisual;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class NPCManager extends AbstractVisualsManager<NPC> {
    private static NPCManager manager = null;

    public NPCManager() {
        Preconditions.checkArgument(manager == null, "NPCManager has already been initialized!");
        manager = this;
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
        Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!").addVisual(npc.getUuid(), npc);
    }

    public static void removeNPC(UUID uuid) {
        getNPC(uuid).ifPresent(NPCManager::removeNPC);
    }

    public static void removeNPC(NPC npc) {
        Preconditions.checkNotNull(manager, "NPCManager is not initialized yet!").removeVisual(npc.getUuid());
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

        final var player = event.getPlayer();
        for (final var npc : activeVisuals.values()) {
            if (!npc.isShown() || !npc.shouldLookAtPlayer()
                    || !npc.getViewers().contains(player) || !player.getLocation().isWorldSame(npc.getLocation())
                    || npc.getLocation().getDistanceSquared(player.getLocation()) > LocatableVisual.DEFAULT_VIEW_DISTANCE) {
                continue;
            }
            npc.lookAtLocation(event.getNewLocation(), player);
        }
    }

    @Override
    public void fireVisualTouchEvent(PlayerWrapper sender, NPC visual, InteractType interactType) {
        EventManager.fireAsync(new NPCInteractEvent(sender, visual, interactType));
    }
}
