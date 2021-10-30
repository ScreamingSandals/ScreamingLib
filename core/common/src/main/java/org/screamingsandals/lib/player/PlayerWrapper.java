package org.screamingsandals.lib.player;

import io.netty.channel.Channel;
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

import java.util.Optional;

/**
 * <p>Class representing a player.</p>
 */
public interface PlayerWrapper extends SenderWrapper, OfflinePlayerWrapper, EntityHuman {

    /**
     * <p>Checks if the player is sprinting.</p>
     *
     * @return is the player sprinting?
     */
    boolean isSprinting();

    /**
     * <p>Sets the player's sprinting status.</p>
     *
     * @param sprinting the new sprinting status
     */
    void setSprinting(boolean sprinting);

    /**
     * <p>Checks if the player is flying.</p>
     *
     * @return is the player flying?
     */
    boolean isFlying();

    /**
     * <p>Sets the player's flying status.</p>
     *
     * @param flying the new flying status
     */
    void setFlying(boolean flying);

    /**
     * <p>Checks if the player flying is allowed.</p>
     *
     * @return is the player flying allowed?
     */
    boolean isAllowFlight();

    /**
     * <p>Sets the player's flying status.</p>
     *
     * @param flying is the player flying allowed
     */
    void setAllowFlight(boolean flying);

    /**
     * <p>Checks if the player is sneaking.</p>
     *
     * @return is the player sneaking?
     */
    boolean isSneaking();

    /**
     * <p>Sets the player's sneaking status.</p>
     *
     * @param sneaking the new sneaking status
     */
    void setSneaking(boolean sneaking);

    /**
     * <p>Gets the player's accurate network latency to the server.</p>
     *
     * @return the player's ping
     */
    int getPing();

    /**
     * <p>Gets the player's tab list name.</p>
     *
     * @return the player's tab list name
     */
    @Nullable
    Component getPlayerListName();

    /**
     * <p>Sets the player's display name.</p>
     *
     * @param component the display name component
     */
    void setPlayerListName(@Nullable Component component);
    /**
     * <p>Sets the player's display name.</p>
     *
     * @param component the display name component
     */
    void setPlayerListName(@Nullable ComponentLike component);

    /**
     * <p>Gets the player's display name.</p>
     *
     * @return the player's display name
     */
    @NotNull
    Component getDisplayName();

    /**
     * <p>Sets the player's display name.</p>
     *
     * @param component the display name component
     */
    void setDisplayName(@Nullable Component component);

    /**
     * <p>Sets the player's display name.</p>
     *
     * @param component the display name component
     */
    void setDisplayName(@Nullable ComponentLike component);

    /**
     * <p>Converts this player to an {@link EntityHuman}.</p>
     *
     * @return the player as an entity
     * @deprecated PlayerWrapper is now instance of {@link EntityHuman}
     */
    @Deprecated
    default EntityHuman asEntity() {
        return this;
    }

    /**
     * <p>Gets the player's ender chest container.</p>
     *
     * @return the player's ender chest
     */
    Container getEnderChest();

    /**
     * <p>Gets the player's inventory container.</p>
     *
     * @return the player's inventory
     */
    PlayerContainer getPlayerInventory();

    /**
     * <p>Gets the inventory that this player has currently opened.</p>
     *
     * @return the inventory the player has currently opened, empty if the player doesn't have an inventory opened
     */
    Optional<Container> getOpenedInventory();

    /**
     * <p>Opens the supplied inventory container for this player.</p>
     *
     * @param container the inventory container
     */
    void openInventory(Openable container);

    /**
     * <p>Closes the inventory that the player has currently opened.</p>
     */
    void closeInventory();

    /**
     * <p>Kicks this player with a message.</p>
     *
     * @param message the kick message
     */
    void kick(Component message);

    /**
     * <p>Kicks this player with a message.</p>
     *
     * @param message the kick message
     */
    void kick(ComponentLike message);

    /**
     * <p>Gets the player's current gamemode.</p>
     *
     * @return the player's gamemode
     */
    GameModeHolder getGameMode();

    /**
     * <p>Sets the gamemode for this player.</p>
     *
     * @param gameMode the new gamemode holder
     */
    void setGameMode(@NotNull GameModeHolder gameMode);

    /**
     * <p>Gets the experience level of this player.</p>
     *
     * @return the player's level
     */
    int getLevel();

    /**
     * <p>Gets the player's experience.</p>
     *
     * @return the player's experience
     */
    float getExp();

    /**
     * <p>Sets the experience level of this player.</p>
     *
     * @param level the player's new experience level
     */
    void setLevel(int level);

    /**
     * <p>Sets the player's experience.</p>
     *
     * @param exp the player's new experience
     */
    void setExp(float exp);

    /**
     * Forces an update of the player's entire inventory.
     *
     * On some platforms it can be useless.
     */
    void forceUpdateInventory();

    Optional<WeatherHolder> getPlayerWeather();

    void setPlayerWeather(@Nullable WeatherHolder weather);

    long getPlayerTime();

    void setPlayerTime(long time, boolean relative);

    void resetPlayerTime();

    void sendParticle(ParticleHolder particle, LocationHolder location);

    /**
     * <p>Gets the player's connection.</p>
     *
     * @return the player connection
     * @see Channel
     */
    Channel getChannel();

    /**
     * <p>Launches the player in it's facing direction.</p>
     *
     * @param multiply the velocity multiplier
     * @param y the y velocity
     */
    default void launch(double multiply, double y) {
        if (isOnline()) {
            setVelocity(getVelocity().multiply(multiply).setY(y));

            EventManager.getDefaultEventManager().registerOneTime(SEntityDamageEvent.class, event -> {
                if (!(event.getEntity() instanceof PlayerWrapper) || !equals(event.getEntity()) || !event.getDamageCause().is("FALL")) {
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
    default void launch(Vector3D velocity) {
        if (isOnline()) {
            setVelocity(velocity);

            EventManager.getDefaultEventManager().registerOneTime(SEntityDamageEvent.class, event -> {
                if (!(event.getEntity() instanceof PlayerWrapper) || !equals(event.getEntity()) || !event.getDamageCause().is("FALL")) {
                    return false;
                }
                event.setCancelled(true);
                return true;
            });
        }
    }

    @Override
    default Type getType() {
        return Type.PLAYER;
    }

    /**
     * Wrapper for hands
     */
    // TODO: holder?
    enum Hand implements Wrapper {
        MAIN,
        OFF;

        @Override
        public <T> T as(Class<T> type) {
            return PlayerMapper.convertHand(this, type);
        }
    }
}
