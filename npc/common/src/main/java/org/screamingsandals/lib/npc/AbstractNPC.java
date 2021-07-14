package org.screamingsandals.lib.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.world.LocationHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractNPC implements NPC {
    private final UUID uuid = new UUID(new Random().nextLong(), 0);
    private final List<TextEntry> text = new ArrayList<>();
    private final List<PlayerWrapper> visibleTo = new ArrayList<>();
    private boolean visible;
    private LocationHolder location;
    private boolean created = false;
    private boolean destroyed = false;
    protected boolean ready = false;
    private final String name = uuid.toString().replace("-", "").substring(0, 10);
    private final GameProfile gameProfile = new GameProfile(uuid, name);
    private NPCSkin skin;
    private boolean shouldLookAtPlayer = false;
    private int viewDistance = NPC.DEFAULT_VIEW_DISTANCE;
    private long clickCoolDown = NPC.CLICK_COOL_DOWN;

    protected AbstractNPC(LocationHolder location) {
        this.location = location;
    }

    @Override
    @NotNull
    public LocationHolder getLocation() {
        return location;
    }

    @Override
    public NPC setLocation(LocationHolder location) {
        if (location == null) {
            throw new UnsupportedOperationException("Location cannot be null!");
        }
        this.location = location;
        return this;
    }

    @Override
    public NPC update() {
        if (ready) {
            update0();
        }
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GameProfile getGameProfile() {
        return gameProfile;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public List<TextEntry> getDisplayName() {
        return List.copyOf(text);
    }

    @Override
    public NPC setDisplayName(List<TextEntry> name) {
        this.text.clear();
        this.text.addAll(name);
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
    }

    @Override
    public NPC addViewer(PlayerWrapper viewer) {
        visibleTo.add(viewer);
        onViewerAdded(viewer);
        return this;
    }

    @Override
    public NPC removeViewer(PlayerWrapper viewer) {
        visibleTo.remove(viewer);
        onViewerRemoved(viewer);
        return this;
    }

    @Override
    public NPC show() {
        if (visible) {
            return this;
        }

        ready = true;
        visible = true;
        return this;
    }

    @Override
    public NPC hide() {
        if (!visible) {
            return this;
        }

        visible = false;
        ready = false;
        visibleTo.clear();
        return this;
    }

    @Override
    public List<PlayerWrapper> getViewers() {
        return List.copyOf(visibleTo);
    }

    @Override
    public NPC setSkin(@Nullable NPCSkin skin) {
        this.skin = skin;
        if (skin == null) {
            gameProfile.getProperties().get("textures").clear();
            return this;
        }
        gameProfile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
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
    public long getClickCoolDown() {
        return clickCoolDown;
    }

    public abstract void onViewerAdded(PlayerWrapper player);

    public abstract void onViewerRemoved(PlayerWrapper player);

    public abstract void update0();
}
