package org.screamingsandals.lib.player;

import lombok.Getter;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.material.container.Openable;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;
import java.util.UUID;

public class PlayerWrapper extends SenderWrapper {
    @Getter
    private final UUID uuid;

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
