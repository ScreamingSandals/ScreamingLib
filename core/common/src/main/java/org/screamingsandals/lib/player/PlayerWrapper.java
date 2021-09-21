package org.screamingsandals.lib.player;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.Openable;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.entity.EntityLiving;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.entity.SEntityDamageEvent;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

    /**
     * <p>Gets the player's target.</p>
     *
     * @return the player's target (the living entity the player is looking at)
     */
    public Optional<EntityLiving> getTarget() {
        return getTarget(3);
    }

    /**
     * <p>Gets the player's target.</p>
     *
     * @param radius the max distance that the target can be detected from
     * @return the player's target (the living entity the player is looking at)
     */
    public Optional<EntityLiving> getTarget(int radius) {
        for (EntityLiving e : getLocation().getNearbyEntitiesByClass(EntityLiving.class, radius)) {
            final LocationHolder eye = asEntity().getEyeLocation();
            final double dot = e.getLocation().asVector().subtract(eye.asVector()).normalize().dot(eye.getFacingDirection());
            if (dot > 0.99D) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    public boolean isSprinting() {
        return PlayerMapper.isSprinting(this);
    }

    public void setSprinting(boolean sprinting) {
        PlayerMapper.setSprinting(this, sprinting);
    }

    public int getPing() {
        return PlayerMapper.getPing(this);
    }

    @NotNull
    public Component getDisplayName() {
        return PlayerMapper.getDisplayName(this);
    }

    public void setDisplayName(@Nullable Component component) {
        PlayerMapper.setDisplayName(this, component);
    }

    public void setDisplayName(@Nullable ComponentLike component) {
        PlayerMapper.setDisplayName(this, component != null ? component.asComponent() : null);
    }

    public EntityHuman asEntity() {
        return as(EntityHuman.class);
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

    public CompletableFuture<Void> teleport(LocationHolder location, Runnable callback) {
        return PlayerMapper.teleport(this, location, callback, false);
    }

    public CompletableFuture<Void> teleport(LocationHolder location, Runnable callback, boolean forceCallback) {
        return PlayerMapper.teleport(this, location, callback, forceCallback);
    }

    public CompletableFuture<Boolean> teleport(LocationHolder location) {
        return PlayerMapper.teleport(this, location);
    }

    public void kick(Component message) {
        PlayerMapper.kick(this, message);
    }

    public void kick(ComponentLike message) {
        PlayerMapper.kick(this, message.asComponent());
    }

    public void setGameMode(@NotNull GameModeHolder gameMode) {
        PlayerMapper.setGameMode(this, gameMode);
    }

    public GameModeHolder getGameMode() {
        return PlayerMapper.getGameMode(this);
    }

    public int getLevel() {
        return PlayerMapper.getLevel(this);
    }

    public float getExp() {
        return PlayerMapper.getExp(this);
    }

    public void setLevel(int level) {
        PlayerMapper.setLevel(this, level);
    }

    public void setExp(float exp) {
        PlayerMapper.setExp(this, exp);
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

    public Optional<WeatherHolder> getPlayerWeather() {
        return PlayerMapper.getWeather(this);
    }

    public void setPlayerWeather(@Nullable WeatherHolder weather) {
        PlayerMapper.setWeather(this, weather);
    }

    public long getPlayerTime() {
        return PlayerMapper.getTime(this);
    }

    public void setPlayerTime(long time, boolean relative) {
        PlayerMapper.setTime(this, time, relative);
    }

    public void resetPlayerTime() {
        PlayerMapper.resetTime(this);
    }

    public void sendParticle(ParticleHolder particle, LocationHolder location) {

    }

    @Override
    public void setWhitelisted(boolean whitelisted) {
        PlayerMapper.setWhitelisted(this, whitelisted);
    }

    public void launch(double multiply, double y) {
        if (isOnline()) {
            var entity = as(EntityHuman.class);
            entity.setVelocity(entity.getVelocity().multiply(multiply).setY(y));

            EventManager.getDefaultEventManager().registerOneTime(SEntityDamageEvent.class, event -> {
                if (!(event.getEntity() instanceof EntityHuman) || !equals(((EntityHuman) event.getEntity()).asPlayer()) || !event.getDamageCause().is("FALL")) {
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

            EventManager.getDefaultEventManager().registerOneTime(SEntityDamageEvent.class, event -> {
                if (!(event.getEntity() instanceof EntityHuman) || !equals(((EntityHuman) event.getEntity()).asPlayer()) || !event.getDamageCause().is("FALL")) {
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
    // TODO: holder?
    public enum Hand implements Wrapper {
        MAIN,
        OFF;

        @Override
        public <T> T as(Class<T> type) {
            return PlayerMapper.convertHand(this, type);
        }
    }
}
