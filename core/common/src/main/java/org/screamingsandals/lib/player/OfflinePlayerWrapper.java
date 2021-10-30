package org.screamingsandals.lib.player;

import org.screamingsandals.lib.sender.MultiPlatformOfflinePlayer;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;

public interface OfflinePlayerWrapper extends MultiPlatformOfflinePlayer {
    /**
     * <p>Gets the location of the bed of this player.</p>
     *
     * @return the location of the bed of this player, can be empty if the player does not have a bed
     */
    default Optional<LocationHolder> getBedLocation() {
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
