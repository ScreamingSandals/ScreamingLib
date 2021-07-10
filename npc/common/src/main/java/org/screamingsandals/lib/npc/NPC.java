package org.screamingsandals.lib.npc;

import com.mojang.authlib.GameProfile;
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
     * Creates new NPC.
     *
     * @param location location where to create the NPC
     * @return created NPC
     */
    static NPC of(LocationHolder location) {
        return NPCManager.npc(location);
    }

    /**
     * Spawns the NPC to all visible Players.
     * @throws UnsupportedOperationException if the NPC has already been spawned
     * @return this NPC
     */
    NPC spawn();

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
     * @return this NPC
     */
    NPC setLocation(LocationHolder location);

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
     * @return this NPC
     */
    NPC setDisplayName(List<TextEntry> name);

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
     * @param viewer the viewer to display the NPC to
     * @return this NPC
     */
    NPC addViewer(PlayerWrapper viewer);

    /**
     *
     * @param viewer the viewer to hide the NPC from.
     * @return this NPC
     */
    NPC removeViewer(PlayerWrapper viewer);

    /**
     *
     * @return a list of current viewers for the NPC
     */
    List<PlayerWrapper> getViewers();

    /**
     * Shows the NPC to all current viewers
     * @return this NPC
     */
    NPC show();

    /**
     * Hides the NPC to all viewers
     * @return this NPC
     */
    NPC hide();

    /**
     * Sets the skin of the NPC
     * @param skin the skin to be set
     * @return this NPC
     */
    NPC setSkin(NPCSkin skin);

    /**
     * Updates the current NPC instance
     * @return this NPC
     */
    NPC update();

    /**
     * Makes the npc look at the particular location
     * @param location the location to look at
     */
    void rotateHead(LocationHolder location);

    /**
     *
     * @return the current game profile object of the npc
     */
    GameProfile getGameProfile();

    /**
     *
     * @return
     */
    String getName();
}
