/*
 * Copyright 2024 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.screamingsandals.lib.player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.api.types.PlayerHolder;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.Openable;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.entity.Entity;
import org.screamingsandals.lib.entity.HumanEntity;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.entity.EntityDamageEvent;
import org.screamingsandals.lib.particle.Particle;
import org.screamingsandals.lib.player.gamemode.GameMode;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.ComponentLike;
import org.screamingsandals.lib.spectator.audience.PlayerAudience;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.Location;
import org.screamingsandals.lib.world.weather.WeatherType;

import java.net.InetSocketAddress;

/**
 * A player representation.
 */
public interface Player extends Sender, OfflinePlayer, HumanEntity, PlayerAudience.ForwardingToAdapter, PlayerHolder {

    /**
     * Checks if the player is sprinting.
     *
     * @return is the player sprinting?
     */
    boolean isSprinting();

    /**
     * Sets the player's sprinting status.
     *
     * @param sprinting the new sprinting status
     */
    void setSprinting(boolean sprinting);

    /**
     * Checks if the player is flying.
     *
     * @return is the player flying?
     */
    boolean isFlying();

    /**
     * Sets the player's flying status.
     *
     * @param flying the new flying status
     */
    void setFlying(boolean flying);

    /**
     * Checks if the player flying is allowed.
     *
     * @return is the player flying allowed?
     */
    boolean isAllowFlight();

    /**
     * Sets the player's flying status.
     *
     * @param flying is the player flying allowed
     */
    void setAllowFlight(boolean flying);

    /**
     * Checks if the player is sneaking.
     *
     * @return is the player sneaking?
     */
    boolean isSneaking();

    /**
     * Sets the player's sneaking status.
     *
     * @param sneaking the new sneaking status
     */
    void setSneaking(boolean sneaking);

    /**
     * Gets the player's accurate network latency to the server.
     *
     * @return the player's ping
     */
    int getPing();

    /**
     * Gets the player's tab list name.
     *
     * @return the player's tab list name
     */
    @Nullable Component getPlayerListName();

    /**
     * Sets the player's display name.
     *
     * @param component the display name component
     */
    void setPlayerListName(@Nullable Component component);

    /**
     * Sets the player's display name.
     *
     * @param component the display name component
     */
    void setPlayerListName(@Nullable ComponentLike component);

    /**
     * Gets the player's display name.
     *
     * @return the player's display name
     */
    @NotNull Component getDisplayName();

    /**
     * Sets the player's display name.
     *
     * @param component the display name component
     */
    void setDisplayName(@Nullable Component component);

    /**
     * Sets the player's display name.
     *
     * @param component the display name component
     */
    void setDisplayName(@Nullable ComponentLike component);

    /**
     * Gets the player's ender chest container.
     *
     * @return the player's ender chest
     */
    @NotNull Container getEnderChest();

    /**
     * Gets the player's inventory container.
     *
     * @return the player's inventory
     */
    @NotNull PlayerContainer getPlayerInventory();

    /**
     * Gets the inventory that this player has currently opened.
     *
     * @return the inventory the player has currently opened, empty if the player doesn't have an inventory opened
     */
    @Nullable Container getOpenedInventory();

    /**
     * Opens the supplied inventory container for this player.
     *
     * @param container the inventory container
     */
    void openInventory(@NotNull Openable container);

    /**
     * Closes the inventory that the player has currently opened.
     */
    void closeInventory();

    /**
     * Kicks this player with a message.
     *
     * @param message the kick message
     */
    void kick(@Nullable Component message);

    /**
     * Kicks this player with a message.
     *
     * @param message the kick message
     */
    void kick(@Nullable ComponentLike message);

    /**
     * Gets the player's current gamemode.
     *
     * @return the player's gamemode
     */
    @NotNull GameMode getGameMode();

    /**
     * Sets the gamemode for this player.
     *
     * @param gameMode the new gamemode holder
     */
    void setGameMode(@NotNull GameMode gameMode);

    /**
     * Gets the experience level of this player.
     *
     * @return the player's level
     */
    int getLevel();

    /**
     * Gets the player's experience.
     *
     * @return the player's experience
     */
    float getExp();

    /**
     * Sets the experience level of this player.
     *
     * @param level the player's new experience level
     */
    void setLevel(int level);

    /**
     * Sets the player's experience.
     *
     * @param exp the player's new experience
     */
    void setExp(float exp);

    /**
     * Forces an update of the player's entire inventory.
     * <p>
     * On some platforms it can be useless.
     */
    void forceUpdateInventory();

    /**
     * Gets the weather for this player.
     *
     * @return the weather for this player
     */
    @Nullable WeatherType getPlayerWeather();

    /**
     * Sets the weather for this player.
     *
     * @param weather the weather
     */
    void setPlayerWeather(@Nullable WeatherType weather);

    long getPlayerTime();

    void setPlayerTime(long time, boolean relative);

    void resetPlayerTime();

    /**
     * Shows a particle to this player.
     *
     * @param particle the particle
     * @param location the location of the particle
     */
    void sendParticle(@NotNull Particle particle, @NotNull Location location);

    /**
     * Gets the compass target location for this player.
     *
     * @return the compass target location
     */
    @NotNull Location getCompassTarget();

    /**
     * Sets the compass target for this player.
     *
     * @param location the compass target location
     */
    void setCompassTarget(@NotNull Location location);

    /**
     * Resets the scoreboard for this player.
     */
    void restoreDefaultScoreboard();

    /**
     * Gets the spectator target for this player.
     *
     * @return the spectator target, empty if there is no target
     */
    @Nullable Entity getSpectatorTarget();

    /**
     * Sets the spectator target for this player.
     *
     * @param entity the spectator target, null to clear the target
     */
    void setSpectatorTarget(@Nullable Entity entity);

    /**
     * Gets the players total experience points.
     * <br>
     * This refers to the total amount of experience the player has collected
     * over time.
     *
     * @return Current total experience points
     */
    int getTotalExperience();

    /**
     * Sets the players current experience points.
     * <br>
     * This refers to the total amount of experience the player has collected
     * over time.
     *
     * @param exp New total experience points
     */
    void setTotalExperience(int exp);

    /**
     * Hides a player from this player.
     *
     * @param player the player who will not be visible by this player
     */
    void hidePlayer(@NotNull Player player);

    /**
     * Allows this player to see a player that was previously hidden.
     * <p>
     * If another plugin had hidden the player too, then the player will
     * remain hidden until the other plugin calls this method too.
     *
     * @param player the player who will be visible by this player
     */
    void showPlayer(@NotNull Player player);

    /**
     * Gets the player's protocol version.
     *
     * @return the protocol version the player's client is connected with
     */
    int getProtocolVersion();

    /**
     * Retrieves players address.
     *
     * @return address
     */
    @Nullable InetSocketAddress getAddress();

    /**
     * Respawns the player.
     */
    void respawn();

    /**
     * Launches the player in its facing direction.
     *
     * @param multiply the velocity multiplier
     * @param y        the y velocity
     */
    default void launch(double multiply, double y) {
        if (isOnline()) {
            setVelocity(getVelocity().multiply(multiply).setY(y));

            EventManager.getDefaultEventManager().registerOneTime(EntityDamageEvent.class, event -> {
                if (!(event.entity() instanceof Player) || !equals(event.entity()) || !event.damageCause().is("FALL")) {
                    return false;
                }
                event.cancelled(true);
                return true;
            });
        }
    }

    /**
     * Launches the player in its facing direction.
     *
     * @param velocity the new velocity vector
     */
    default void launch(@NotNull Vector3D velocity) {
        if (isOnline()) {
            setVelocity(velocity);

            EventManager.getDefaultEventManager().registerOneTime(EntityDamageEvent.class, event -> {
                if (!(event.entity() instanceof Player) || !equals(event.entity()) || !event.damageCause().is("FALL")) {
                    return false;
                }
                event.cancelled(true);
                return true;
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default @NotNull Type getType() {
        return Type.PLAYER;
    }
}
