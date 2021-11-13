package org.screamingsandals.lib.npc;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.npc.skin.NPCSkin;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.visuals.TouchableVisual;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;

/**
 * Represents the API for NPCs.
 */
public interface NPC extends TouchableVisual<NPC> {

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
     *
     * @return a list containing text entries for the NPC name, null if not set
     */
    @Nullable
    List<TextEntry> getDisplayName();

    NPCSkin getSkin();

    /**
     *
     * @param name name (multi-lined) of the NPC.
     * @return this NPC
     */
    NPC setDisplayName(List<Component> name);

    /**
     *
     * @return id of the NPC
     */
    int getEntityId();


    /**
     * Sets the skin of the NPC
     * @param skin the skin to be set
     * @return this NPC
     */
    NPC setSkin(NPCSkin skin);

    /**
     * Makes the npc look at the particular location
     * @param location the location to look at
     */
    void lookAtPlayer(LocationHolder location, PlayerWrapper player);

    /**
     *
     * @return single line name of entity generated from uuid
     */
    Component getName();

    /**
     *
     * @param shouldLook if true the npc will rotate its head towards the viewer
     * @return this NPC
     */
    NPC setShouldLookAtPlayer(boolean shouldLook);

    /**
     *
     * @return true if NPC should look at the player, false otherwise
     */
    boolean shouldLookAtPlayer();

    /**
     *
     * @return the hologram instance that displays the name of the entity.
     */
    Hologram getHologram();
}
