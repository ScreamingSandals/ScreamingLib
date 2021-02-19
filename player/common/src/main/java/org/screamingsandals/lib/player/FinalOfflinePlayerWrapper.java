package org.screamingsandals.lib.player;

import lombok.Data;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.Optional;
import java.util.UUID;

@Data
public class FinalOfflinePlayerWrapper implements OfflinePlayerWrapper {
    private final UUID uuid;
    private final String name;

    public Optional<LocationHolder> getBedLocation() {
        return PlayerMapper.getBedLocation(this);
    }

    @Override
    public Optional<String> getLastName() {
        return Optional.ofNullable(name);
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

    @Override
    public boolean isOp() {
        return PlayerMapper.isOp(this);
    }

    @Override
    public void setOp(boolean op) {
        PlayerMapper.setOp(this, op);
    }

    @Override
    public <T> T as(Class<T> type) {
        return PlayerMapper.convertOfflinePlayer(this, type);
    }
}
