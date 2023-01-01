/*
 * Copyright 2023 ScreamingSandals
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

import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.world.LocationHolder;

/**
 * A representation of an offline player.
 */
public interface OfflinePlayerWrapper extends MultiPlatformOfflinePlayer {
    /**
     * Gets the location of the bed of this player.
     *
     * @return the location, can be empty if the player does not have a bed
     */
    default @Nullable LocationHolder getBedLocation() {
        return PlayerMapper.getBedLocation(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default long getFirstPlayed() {
        return PlayerMapper.getFirstPlayed(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default long getLastPlayed() {
        return PlayerMapper.getLastPlayed(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isBanned() {
        return PlayerMapper.isBanned(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isWhitelisted() {
        return PlayerMapper.isWhitelisted(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void setWhitelisted(boolean whitelisted) {
        PlayerMapper.setWhitelisted(this, whitelisted);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isOp() {
        return PlayerMapper.isOp(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void setOp(boolean op) {
        PlayerMapper.setOp(this, op);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default boolean isOnline() {
        return PlayerMapper.isOnline(this);
    }
}
