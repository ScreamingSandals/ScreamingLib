package org.screamingsandals.lib.player;

import io.netty.channel.Channel;
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

/**
 * <p>Class representing a player.</p>
 */
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
     * <p>Checks if the player is on ground.</p>
     *
     * @return is the player on ground?
     */
    public boolean isOnGround() {
        return asEntity().getVelocity().getY() == 0 && !getLocation().remove(0, 1, 0).getBlock().isEmpty();
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

    /**
     * <p>Checks if the player is sprinting.</p>
     *
     * @return is the player sprinting?
     */
    public boolean isSprinting() {
        return PlayerMapper.isSprinting(this);
    }

    /**
     * <p>Sets the player's sprinting status.</p>
     *
     * @param sprinting the new sprinting status
     */
    public void setSprinting(boolean sprinting) {
        PlayerMapper.setSprinting(this, sprinting);
    }

    /**
     * <p>Checks if the player is flying.</p>
     *
     * @return is the player flying?
     */
    public boolean isFlying() {
        return PlayerMapper.isFlying(this);
    }

    /**
     * <p>Sets the player's flying status.</p>
     *
     * @param flying the new flying status
     */
    public void setFlying(boolean flying) {
        PlayerMapper.setFlying(this, flying);
    }

    /**
     * <p>Checks if the player flying is allowed.</p>
     *
     * @return is the player flying allowed?
     */
    public boolean isAllowFlight() {
        return PlayerMapper.isAllowFlight(this);
    }

    /**
     * <p>Sets the player's flying status.</p>
     *
     * @param flying is the player flying allowed
     */
    public void setAllowFlight(boolean flying) {
        PlayerMapper.setAllowFlight(this, flying);
    }

    /**
     * <p>Checks if the player is sneaking.</p>
     *
     * @return is the player sneaking?
     */
    public boolean isSneaking() {
        return PlayerMapper.isSneaking(this);
    }

    /**
     * <p>Sets the player's sneaking status.</p>
     *
     * @param sneaking the new sneaking status
     */
    public void setSneaking(boolean sneaking) {
        PlayerMapper.setSneaking(this, sneaking);
    }

    /**
     * <p>Gets the player's accurate network latency to the server.</p>
     *
     * @return the player's ping
     */
    public int getPing() {
        return PlayerMapper.getPing(this);
    }

    /**
     * <p>Gets the player's tab list name.</p>
     *
     * @return the player's tab list name
     */
    @Nullable
    public Component getPlayerListName() {
        return PlayerMapper.getPlayerListName(this);
    }

    /**
     * <p>Sets the player's display name.</p>
     *
     * @param component the display name component
     */
    public void setPlayerListName(@Nullable Component component) {
        PlayerMapper.setPlayerListName(this, component);
    }

    /**
     * <p>Sets the player's display name.</p>
     *
     * @param component the display name component
     */
    public void setPlayerListName(@Nullable ComponentLike component) {
        PlayerMapper.setPlayerListName(this, component != null ? component.asComponent() : null);
    }

    /**
     * <p>Gets the player's display name.</p>
     *
     * @return the player's display name
     */
    @NotNull
    public Component getDisplayName() {
        return PlayerMapper.getDisplayName(this);
    }

    /**
     * <p>Sets the player's display name.</p>
     *
     * @param component the display name component
     */
    public void setDisplayName(@Nullable Component component) {
        PlayerMapper.setDisplayName(this, component);
    }

    /**
     * <p>Sets the player's display name.</p>
     *
     * @param component the display name component
     */
    public void setDisplayName(@Nullable ComponentLike component) {
        PlayerMapper.setDisplayName(this, component != null ? component.asComponent() : null);
    }

    /**
     * <p>Converts this player to an {@link EntityHuman}.</p>
     *
     * @return the player as an entity
     */
    public EntityHuman asEntity() {
        return as(EntityHuman.class);
    }

    /**
     * <p>Gets the player's ender chest container.</p>
     *
     * @return the player's ender chest
     */
    public Container getEnderChest() {
        return PlayerMapper.getEnderChest(this);
    }

    /**
     * <p>Gets the player's inventory container.</p>
     *
     * @return the player's inventory
     */
    public PlayerContainer getPlayerInventory() {
        return PlayerMapper.getPlayerInventory(this);
    }

    /**
     * <p>Gets the inventory that this player has currently opened.</p>
     *
     * @return the inventory the player has currently opened, empty if the player doesn't have an inventory opened
     */
    public Optional<Container> getOpenedInventory() {
        return PlayerMapper.getOpenedInventory(this);
    }

    /**
     * <p>Opens the supplied inventory container for this player.</p>
     *
     * @param container the inventory container
     */
    public void openInventory(Openable container) {
        container.openInventory(this);
    }

    /**
     * <p>Closes the inventory that the player has currently opened.</p>
     */
    public void closeInventory() {
        PlayerMapper.closeInventory(this);
    }

    /**
     * <p>Gets the player's location.</p>
     *
     * @return the player's location
     */
    public LocationHolder getLocation() {
        return PlayerMapper.getLocation(this);
    }

    /**
     * <p>Teleports this player to a location asynchronously.</p>
     *
     * @param location the location to teleport to
     * @param callback the callback runnable
     * @return the teleport future
     */
    public CompletableFuture<Void> teleport(LocationHolder location, Runnable callback) {
        return PlayerMapper.teleport(this, location, callback, false);
    }

    /**
     * <p>Teleports this player to a location asynchronously.</p>
     *
     * @param location the location to teleport to
     * @param callback the callback runnable
     * @param forceCallback should the callback be run even if the teleport didn't succeed?
     * @return the teleport future
     */
    public CompletableFuture<Void> teleport(LocationHolder location, Runnable callback, boolean forceCallback) {
        return PlayerMapper.teleport(this, location, callback, forceCallback);
    }

    /**
     * <p>Teleports this player to a location asynchronously.</p>
     *
     * @param location the location to teleport to
     * @return the teleport future
     */
    public CompletableFuture<Boolean> teleport(LocationHolder location) {
        return PlayerMapper.teleport(this, location);
    }

    /**
     * <p>Kicks this player with a message.</p>
     *
     * @param message the kick message
     */
    public void kick(Component message) {
        PlayerMapper.kick(this, message);
    }

    /**
     * <p>Kicks this player with a message.</p>
     *
     * @param message the kick message
     */
    public void kick(ComponentLike message) {
        PlayerMapper.kick(this, message.asComponent());
    }

    /**
     * <p>Gets the player's current gamemode.</p>
     *
     * @return the player's gamemode
     */
    public GameModeHolder getGameMode() {
        return PlayerMapper.getGameMode(this);
    }

    /**
     * <p>Sets the gamemode for this player.</p>
     *
     * @param gameMode the new gamemode holder
     */
    public void setGameMode(@NotNull GameModeHolder gameMode) {
        PlayerMapper.setGameMode(this, gameMode);
    }

    /**
     * <p>Gets the experience level of this player.</p>
     *
     * @return the player's level
     */
    public int getLevel() {
        return PlayerMapper.getLevel(this);
    }

    /**
     * <p>Gets the player's experience.</p>
     *
     * @return the player's experience
     */
    public float getExp() {
        return PlayerMapper.getExp(this);
    }

    /**
     * <p>Sets the experience level of this player.</p>
     *
     * @param level the player's new experience level
     */
    public void setLevel(int level) {
        PlayerMapper.setLevel(this, level);
    }

    /**
     * <p>Sets the player's experience.</p>
     *
     * @param exp the player's new experience
     */
    public void setExp(float exp) {
        PlayerMapper.setExp(this, exp);
    }

    /**
     * Forces an update of the player's entire inventory.
     *
     * On some platforms it can be useless.
     */
    public void forceUpdateInventory() {
        PlayerMapper.forceUpdateInventory(this);
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
     * <p>Gets the location of the bed of this player.</p>
     *
     * @return the location of the bed of this player, can be empty if the player does not have a bed
     */
    @Override
    public Optional<LocationHolder> getBedLocation() {
        return PlayerMapper.getBedLocation(this);
    }

    /**
     * <p>Gets the player's last name.</p>
     *
     * @return the player's last name
     */
    @Override
    public Optional<String> getLastName() {
        return Optional.ofNullable(getName());
    }

    /**
     * <p>Gets the first played time.</p>
     *
     * @return the first played time
     */
    @Override
    public long getFirstPlayed() {
        return PlayerMapper.getFirstPlayed(this);
    }

    /**
     * <p>Gets the last played time.</p>
     *
     * @return the last played time
     */
    @Override
    public long getLastPlayed() {
        return PlayerMapper.getLastPlayed(this);
    }

    /**
     * <p>Checks if this player is banned.</p>
     *
     * @return is the player banned?
     */
    @Override
    public boolean isBanned() {
        return PlayerMapper.isBanned(this);
    }

    /**
     * <p>Checks if this player is whitelisted.</p>
     *
     * @return is the player whitelisted?
     */
    @Override
    public boolean isWhitelisted() {
        return PlayerMapper.isWhitelisted(this);
    }

    /**
     * <p>Sets the whitelisted status for this player.</p>
     *
     * @param whitelisted the new whitelisted status
     */
    @Override
    public void setWhitelisted(boolean whitelisted) {
        PlayerMapper.setWhitelisted(this, whitelisted);
    }

    /**
     * <p>Checks if this player is online.</p>
     *
     * @return is the player online?
     */
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
        PlayerMapper.sendParticle(this, particle, location);
    }

    /**
     * <p>Gets the player's connection.</p>
     *
     * @return the player connection
     * @see Channel
     */
    public Channel getChannel() {
        return PlayerMapper.getChannel(this);
    }

    /**
     * <p>Launches the player in it's facing direction.</p>
     *
     * @param multiply the velocity multiplier
     * @param y the y velocity
     */
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

    /**
     * <p>Launches the player in it's facing direction.</p>
     *
     * @param velocity the new velocity vector
     */
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
