package org.screamingsandals.lib.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.tasker.Tasker;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.tasker.task.TaskerTask;
import org.screamingsandals.lib.utils.visual.TextEntry;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractNPC implements NPC {
    private final UUID uuid = UUID.randomUUID();
    private final List<TextEntry> text = new ArrayList<>();
    private final List<PlayerWrapper> visibleTo = new ArrayList<>();
    private boolean visible;
    @Nullable
    private LocationHolder location;
    private boolean created = false;
    private boolean destroyed = false;
    protected boolean ready = false;
    private TaskerTask tickTask;
    private String name = uuid.toString().replace("-", "").substring(0, 10);
    private final GameProfile gameProfile = new GameProfile(uuid, name);
    private NPCSkin skin;

    @Override
    @Nullable
    public LocationHolder getLocation() {
        return location;
    }

    @Override
    public void setLocation(LocationHolder location) {
        this.location = location;
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
    public void setDisplayName(List<TextEntry> name) {
        this.text.clear();
        this.text.addAll(name);
    }

    @Override
    public boolean isVisibleToPlayer(PlayerWrapper player) {
        return visibleTo.contains(player);
    }

    @Override
    public void spawn() {
        if (created) {
            throw new UnsupportedOperationException("NPC: " + uuid.toString() + " is already spawned!");
        }

        created = true;
        if (tickTask != null) {
            tickTask.cancel();
        }

        tickTask = Tasker.build(this::tick).repeat(1L, TaskerTime.TICKS).start();
    }

    @Override
    public void tick() {

    }


    public void destroy() {
        if (isDestroyed()) {
            return;
        }

        destroyed = true;
        Tasker.build(() -> NPCRegistry.unregisterNPC(this)).afterOneTick().start();
    }

    @Override
    public void addViewer(PlayerWrapper viewer) {
        visibleTo.add(viewer);
        onViewerAdded(viewer);
    }

    @Override
    public void removeViewer(PlayerWrapper viewer) {
        visibleTo.remove(viewer);
        onViewerRemoved(viewer);
    }

    public abstract void onViewerAdded(PlayerWrapper player);

    public abstract void onViewerRemoved(PlayerWrapper player);

    @Override
    public void show() {
        if (visible) {
            return;
        }

        ready = true;
        visible = true;
    }

    @Override
    public void hide() {
        if (!visible) {
            return;
        }

        visible = false;
        ready = false;
        visibleTo.clear();
    }

    @Override
    public List<PlayerWrapper> getViewers() {
        return List.copyOf(visibleTo);
    }

    @Override
    public void setSkin(@Nullable NPCSkin skin) {
        this.skin = skin;
        if (skin == null) {
            gameProfile.getProperties().get("textures").clear();
            return;
        }
        gameProfile.getProperties().put("textures", new Property(skin.getValue(), skin.getSignature()));
    }
}
