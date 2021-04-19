package org.screamingsandals.lib.player;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.material.container.Container;
import org.screamingsandals.lib.material.container.Openable;
import org.screamingsandals.lib.material.container.PlayerContainer;
import org.screamingsandals.lib.player.event.SPlayerDamageEvent;
import org.screamingsandals.lib.utils.GameMode;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
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

    public Container getEnderChest() {
        return PlayerMapper.getEnderChest(this);
    }

    public PlayerContainer getPlayerInventory() {
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

    public void setGameMode(@NotNull GameMode gameMode) {
        PlayerMapper.setGameMode(this, gameMode);
    }

    public GameMode getGameMode() {
        return PlayerMapper.getGameMode(this);
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

    public void launch(double multiply, double y) {
        if (isOnline()) {
            var entity = as(EntityHuman.class);
            entity.setVelocity(entity.getVelocity().multiply(multiply).setY(y));

            EventManager.getDefaultEventManager().registerOneTime(SPlayerDamageEvent.class, event -> {
                if (!equals(event.getPlayer()) || event.getCause() != SPlayerDamageEvent.DamageCause.FALL) {
                    return false;
                }
                event.setCancelled(true);
                return true;
            });
        }
    }

    public void launch(Vector3D velocity) {
        if (isOnline()) {
            var entity = as(EntityHuman.class);
            entity.setVelocity(velocity);

            EventManager.getDefaultEventManager().registerOneTime(SPlayerDamageEvent.class, event -> {
                if (!equals(event.getPlayer()) || event.getCause() != SPlayerDamageEvent.DamageCause.FALL) {
                    return false;
                }
                event.setCancelled(true);
                return true;
            });
        }
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
