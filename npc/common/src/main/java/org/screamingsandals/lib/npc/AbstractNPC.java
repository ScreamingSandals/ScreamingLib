package org.screamingsandals.lib.npc;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.hologram.HologramManager;
import org.screamingsandals.lib.packet.SClientboundPlayerInfoPacket;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.*;
import java.util.List;

public abstract class AbstractNPC implements NPC {
    protected final UUID uuid;
    protected final List<PlayerWrapper> visibleTo;
    protected LocationHolder location;
    protected boolean created;
    protected boolean destroyed;
    protected boolean ready;
    protected volatile boolean shouldLookAtPlayer;
    protected volatile boolean touchable;
    protected boolean visible;
    protected final String name;
    protected final List<SClientboundPlayerInfoPacket.Property> properties;
    protected NPCSkin skin;
    protected int viewDistance;
    protected long clickCoolDown;

    /**
     * hologram that displays the name of the entity
     */
    private final Hologram hologram;

    protected AbstractNPC(UUID uuid, LocationHolder location, boolean touchable) {
        this.uuid = uuid;
        this.location = location;
        this.touchable = touchable;

        // default values.
        this.created = false;
        this.destroyed = false;
        this.ready = false;
        this.shouldLookAtPlayer = false;
        this.viewDistance = NPC.DEFAULT_VIEW_DISTANCE;
        this.clickCoolDown = NPC.CLICK_COOL_DOWN;
        this.properties = new ArrayList<>();
        this.visibleTo = new ArrayList<>();
        this.name = "[NPC] " + uuid.toString().replace("-", "").substring(0, 10);
        this.hologram = HologramManager.hologram(location.clone().add(0.0D, 1.5D, 0.0D));
    }

    @Override
    public boolean isShown() {
        return visible;
    }

    @Override
    public Hologram getHologram() {
        return hologram;
    }

    @Override
    @NotNull
    public LocationHolder getLocation() {
        return location;
    }

    @Override
    public boolean isTouchable() {
        return touchable;
    }

    @Override
    public NPC setTouchable(boolean touchable) {
        this.touchable = touchable;
        return this;
    }

    @Override
    public NPC setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        this.location = location;
        hologram.setLocation(location.clone().add(0.0D, 1.5D, 0.0D));
        return this;
    }

    @Override
    public NPC update() {
        if (ready) {
            // TODO: update
        }
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public List<TextEntry> getDisplayName() {
        return List.copyOf(hologram.getLines().values());
    }

    @Override
    public NPC setDisplayName(List<Component> name) {
        for (int i = 0; i < name.size(); i++) {
            hologram.replaceLine(i, name.get(i));
        }
        return this;
    }

    @Override
    public boolean isVisibleToPlayer(PlayerWrapper player) {
        return visibleTo.contains(player);
    }

    @Override
    public NPC spawn() {
        if (created) {
            throw new UnsupportedOperationException("NPC: " + uuid.toString() + " is already spawned!");
        }

        created = true;
        return this;
    }

    public void destroy() {
        if (isDestroyed()) {
            return;
        }
        hide();
        getViewers().forEach(this::removeViewer);
        visibleTo.clear();
        destroyed = true;
        NPCManager.removeNPC(this);
        HologramManager.removeHologram(hologram);
    }

    @Override
    public NPC addViewer(PlayerWrapper viewer) {
        visibleTo.add(viewer);
        hologram.addViewer(viewer);
        onViewerAdded(viewer, false);
        return this;
    }

    @Override
    public NPC removeViewer(PlayerWrapper viewer) {
        visibleTo.remove(viewer);
        hologram.removeViewer(viewer);
        onViewerRemoved(viewer, false);
        return this;
    }

    @Override
    public NPC show() {
        if (visible) {
            return this;
        }

        ready = true;
        visible = true;
        hologram.show();
        visibleTo.forEach(viewer -> onViewerAdded(viewer, false));
        return this;
    }

    @Override
    public NPC hide() {
        if (!visible) {
            return this;
        }

        visible = false;
        ready = false;
        hologram.hide();
        visibleTo.forEach(viewer -> onViewerRemoved(viewer, false));
        return this;
    }

    @Override
    public List<PlayerWrapper> getViewers() {
        return List.copyOf(visibleTo);
    }

    @Override
    public NPC setSkin(@Nullable NPCSkin skin) {
        this.skin = skin;
        properties.removeIf(property -> property.name().equals("textures"));
        if (skin == null) {
            return this;
        }
        properties.add(new SClientboundPlayerInfoPacket.Property("textures", skin.getValue(), skin.getSignature()));
        return this;
    }

    @Override
    public NPC setShouldLookAtViewer(boolean shouldLook) {
        this.shouldLookAtPlayer = shouldLook;
        return this;
    }

    @Override
    public boolean shouldLookAtPlayer() {
        return shouldLookAtPlayer;
    }

    @Override
    public boolean hasViewers() {
        return !visibleTo.isEmpty();
    }

    @Override
    public int getViewDistance() {
        return viewDistance;
    }

    @Override
    public NPC setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
        return this;
    }

    @Override
    public NPC setClickCoolDown(long delay) {
        if (delay < 0) {
            return this;
        }
        clickCoolDown = delay;
        return this;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean hasId(int id) {
        return id == getEntityId();
    }

    @Override
    public long getClickCoolDown() {
        return clickCoolDown;
    }
}
