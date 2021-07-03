package org.screamingsandals.lib.npc;

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class NPC {
    private final UUID uuid = UUID.randomUUID();
    private final List<TextEntry> name = new ArrayList<>();
    private final List<PlayerWrapper> visibleTo = new ArrayList<>();
    private boolean visible;
    @Nullable
    private LocationHolder location;

    @Nullable
    public LocationHolder getLocation() {
        return location;
    }

    public LocationHolder setLocation(LocationHolder location) {
        this.location = location;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isVisible() {
        return visible;
    }

    public List<TextEntry> getDisplayName() {
        return List.copyOf(name);
    }

    public void setDisplayName(List<TextEntry> name) {
        this.name.clear();
        this.name.addAll(name);
        setDisplayName0(name);
    }

    public boolean isVisibleToPlayer(PlayerWrapper player) {
        return visibleTo.contains(player);
    }

    protected abstract void setDisplayName0(List<TextEntry> name);

    protected abstract void tick();
}
