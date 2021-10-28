package org.screamingsandals.lib.player;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;
import java.util.UUID;

@Data
public class FinalOfflinePlayerWrapper implements OfflinePlayerWrapper {
    private final UUID uuid;
    @EqualsAndHashCode.Exclude
    private final String name;

    @Override
    public Optional<String> getLastName() {
        return Optional.ofNullable(name);
    }

    @Override
    public <T> T as(Class<T> type) {
        return PlayerMapper.convertOfflinePlayer(this, type);
    }
}
