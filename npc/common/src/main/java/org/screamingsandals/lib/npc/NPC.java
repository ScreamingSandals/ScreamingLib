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
     * Creates a new NPC.
     *
     * @param location the location of the newly created NPC
     * @return the created NPC
     */
    static NPC of(LocationHolder location) {
        return NPCManager.npc(location);
    }

    /**
     * <p>Gets the display name text entries of this NPC.</p>
     *
     * @return a list containing text entries for the NPC name, null if not set
     * @see NPC#setDisplayName(List) for setting the display name
     */
    @Nullable
    List<TextEntry> getDisplayName();

    /**
     * <p>Gets the skin of this NPC.</p>
     *
     * @return the skin of this NPC
     * @see NPC#setSkin(NPCSkin) for setting the skin of this NPC
     */
    NPCSkin getSkin();

    /**
     * <p>Sets the display name text entries of this NPC.</p>
     *
     * @param name name (multi-lined) of the NPC.
     * @return this NPC
     * @see NPC#getDisplayName() for getting the current display name
     */
    NPC setDisplayName(List<Component> name);

    /**
     * <p>Gets the ID of this NPC entity.</p>
     *
     * @return the ID of this NPC entity
     */
    int getEntityId();


    /**
     * Sets the skin of this NPC.
     *
     * @param skin the skin
     * @return this NPC
     * @see NPC#getSkin() for getting the current skin of this NPC
     */
    NPC setSkin(NPCSkin skin);

    /**
     * Makes the NPC look at the specified location in the eyes of the specified player.
     *
     * @param location the location
     */
    void lookAtLocation(LocationHolder location, PlayerWrapper player);

    /**
     * <p>Gets the entity name.</p>
     *
     * @return a single line name of the entity generated from the UUID
     */
    Component getName();

    /**
     * <p>Sets whether the line of sight of this NPC should follow a viewer.</p>
     *
     * @param shouldLook should the NPC rotate its head towards a viewer?
     * @return this NPC
     * @see NPC#shouldLookAtPlayer() for determining the current state
     */
    NPC setShouldLookAtPlayer(boolean shouldLook);

    /**
     * <p>Determines if the line of sight of this NPC is following a viewer.</p>
     *
     * @return is the NPC rotating its head towards the viewer?
     * @see NPC#setShouldLookAtPlayer(boolean) for setting the current state
     */
    boolean shouldLookAtPlayer();

    /**
     * <p>Gets the {@link Hologram} instance that displays the name of this NPC.</p>
     *
     * @return the hologram
     */
    Hologram getHologram();
}
