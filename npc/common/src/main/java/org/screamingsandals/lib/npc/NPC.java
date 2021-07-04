package org.screamingsandals.lib.npc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.UUID;

/**
 * API For the NPC (non-player-character)
 */
public interface NPC {

    /**
     * Spawns the NPC to all visible Players.
     * @throws UnsupportedOperationException if the NPC has already been spawned
     */
    void spawn();

    /**
     * Destroys the NPC entirely.
     */
    void destroy();

    /**
     * Entity logic per tick.
     */
    void tick();

    /**
     *
     * @return current location of the NPC
     */
    @Nullable
    LocationHolder getLocation();

    /**
     *
     * @param location location where to create the NPC
     */
    void setLocation(LocationHolder location);

    /**
     *
     * @return UUID of the NPC
     */
    @NotNull
    UUID getUUID();

    /**
     *
     * @return  true if NPC is visible, false otherwise
     */
    boolean isVisible();

    /**
     *
     * @return a list containing text entries for the NPC name, null if not set
     */
    @Nullable
    List<TextEntry> getDisplayName();

    /**
     *
     * @param name name (multi-lined) of the NPC.
     */
    void setDisplayName(List<TextEntry> name);

    /**
     *
     * @param player the player to query visibility status
     * @return true if current NPC instance is visible to player, false otherwise
     */
    boolean isVisibleToPlayer(PlayerWrapper player);

    /**
     *
     * @return true if NPC has been destroyed, false otherwise
     */
    boolean isDestroyed();

    /**
     *
     * @return id of the NPC
     */
    int getEntityId();

    /**
     *
     * @param viewer
     */
    void addViewer(PlayerWrapper viewer);

    /**
     *
     * @param viewer
     */
    void removeViewer(PlayerWrapper viewer);

    List<PlayerWrapper> getViewers();

    void show();

    void hide();

    void setSkin(NPCSkin skin);
}
