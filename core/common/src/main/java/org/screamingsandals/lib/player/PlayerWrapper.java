/*
 * Copyright 2022 ScreamingSandals
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

import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.container.Container;
import org.screamingsandals.lib.container.Openable;
import org.screamingsandals.lib.container.PlayerContainer;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityHuman;
import org.screamingsandals.lib.event.EventManager;
import org.screamingsandals.lib.event.entity.SEntityDamageEvent;
import org.screamingsandals.lib.particle.ParticleHolder;
import org.screamingsandals.lib.player.gamemode.GameModeHolder;
import org.screamingsandals.lib.utils.Wrapper;
import org.screamingsandals.lib.utils.math.Vector3D;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.weather.WeatherHolder;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;

/**
 * A player representation.
 */
public interface PlayerWrapper extends SenderWrapper, OfflinePlayerWrapper, EntityHuman {
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
    @Nullable
    Component getPlayerListName();

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
    @NotNull
    Component getDisplayName();

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
    Container getEnderChest();

    /**
     * Gets the player's inventory container.
     *
     * @return the player's inventory
     */
    PlayerContainer getPlayerInventory();

    /**
     * Gets the inventory that this player has currently opened.
     *
     * @return the inventory the player has currently opened, empty if the player doesn't have an inventory opened
     */
    Optional<Container> getOpenedInventory();

    /**
     * Opens the supplied inventory container for this player.
     *
     * @param container the inventory container
     */
    void openInventory(Openable container);

    /**
     * Closes the inventory that the player has currently opened.
     */
    void closeInventory();

    /**
     * Kicks this player with a message.
     *
     * @param message the kick message
     */
    void kick(Component message);

    /**
     * Kicks this player with a message.
     *
     * @param message the kick message
     */
    void kick(ComponentLike message);

    /**
     * Gets the player's current gamemode.
     *
     * @return the player's gamemode
     */
    GameModeHolder getGameMode();

    /**
     * Sets the gamemode for this player.
     *
     * @param gameMode the new gamemode holder
     */
    void setGameMode(@NotNull GameModeHolder gameMode);

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
    Optional<WeatherHolder> getPlayerWeather();

    /**
     * Sets the weather for this player.
     *
     * @param weather the weather
     */
    void setPlayerWeather(@Nullable WeatherHolder weather);

    long getPlayerTime();

    void setPlayerTime(long time, boolean relative);

    void resetPlayerTime();

    /**
     * Shows a particle to this player.
     *
     * @param particle the particle
     * @param location the location of the particle
     */
    void sendParticle(ParticleHolder particle, LocationHolder location);

    /**
     * Gets the compass target location for this player.
     *
     * @return the compass target location
     */
    LocationHolder getCompassTarget();

    /**
     * Sets the compass target for this player.
     *
     * @param location the compass target location
     */
    void setCompassTarget(LocationHolder location);

    /**
     * Resets the scoreboard for this player.
     */
    void restoreDefaultScoreboard();

    /**
     * Gets the spectator target for this player.
     *
     * @return the spectator target, empty if there is no target
     */
    Optional<EntityBasic> getSpectatorTarget();

    /**
     * Sets the spectator target for this player.
     *
     * @param entity the spectator target, null to clear the target
     */
    void setSpectatorTarget(@Nullable EntityBasic entity);

    /**
     * Gets the player's {@link Channel}.
     *
     * @return the player connection
     */
    Channel getChannel();

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
    InetSocketAddress getAddress();

    /**
     * Launches the player in its facing direction.
     *
     * @param multiply the velocity multiplier
     * @param y        the y velocity
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
     * Launches the player in its facing direction.
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

    /**
     * {@inheritDoc}
     */
    @Override
    default Type getType() {
        return Type.PLAYER;
    }

    /**
     * Wrapper for hands.
     */
    // TODO: holder?
    enum Hand implements Wrapper {
        MAIN,
        OFF;

        /**
         * {@inheritDoc}
         */
        @Override
        public <T> T as(Class<T> type) {
            return PlayerMapper.convertHand(this, type);
        }
    }
}
