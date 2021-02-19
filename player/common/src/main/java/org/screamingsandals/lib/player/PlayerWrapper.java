package org.screamingsandals.lib.player;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.material.container.Openable;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

public class PlayerWrapper extends SenderWrapper implements OfflinePlayerWrapper {
    @Getter
    private final UUID uuid;
    @Getter
    @Setter
    private WeakReference<Object> wrappedPlayer;

    public PlayerWrapper(String name, UUID uuid) {
        super(name, Type.PLAYER);
        this.uuid = uuid;
    }

    public Container getPlayerInventory() {
        return PlayerMapper.getPlayerInventory(this);
    }

    public Optional<Container> getOpenedInventory() {
        return PlayerMapper.getOpenedInventory(this);
    }

    public void openInventory(Openable container) {
        container.openInventory(this);
    }

    public void closeInventory() {
        PlayerMapper.closeInventory(this);
    }

    public LocationHolder getLocation() {
        return PlayerMapper.getLocation(this);
    }

    public void teleport(LocationHolder location, Runnable callback) {
        PlayerMapper.teleport(this, location, callback);
    }

    public void teleport(LocationHolder location) {
        teleport(location, null);
    }

    public void kick(Component message) {
        PlayerMapper.kick(this, message);
    }

    public <T> T as(Class<T> type) {
        return PlayerMapper.convertPlayerWrapper(this, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerWrapper)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return ((PlayerWrapper) obj).uuid.equals(this.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public Optional<LocationHolder> getBedLocation() {
        return PlayerMapper.getBedLocation(this);
    }

    @Override
    public Optional<String> getLastName() {
        return Optional.ofNullable(getName());
    }

    @Override
    public long getFirstPlayed() {
        return PlayerMapper.getFirstPlayed(this);
    }

    @Override
    public long getLastPlayed() {
        return PlayerMapper.getLastPlayed(this);
    }

    @Override
    public boolean isBanned() {
        return PlayerMapper.isBanned(this);
    }

    @Override
    public boolean isWhitelisted() {
        return PlayerMapper.isWhitelisted(this);
    }

    @Override
    public boolean isOnline() {
        return PlayerMapper.isOnline(this);
    }

    @Override
    public void setWhitelisted(boolean whitelisted) {
        PlayerMapper.setWhitelisted(this, whitelisted);
    }

    /**
     * Wrapper for hands
     */
    public enum Hand implements Wrapper {
        MAIN,
        OFF;

        @Override
        public <T> T as(Class<T> type) {
            return PlayerMapper.convertHand(this, type);
        }
    }
}
