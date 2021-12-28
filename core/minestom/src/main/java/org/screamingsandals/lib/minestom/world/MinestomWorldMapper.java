package org.screamingsandals.lib.minestom.world;

import net.minestom.server.MinecraftServer;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;

import java.util.Optional;
import java.util.UUID;

@Service
public class MinestomWorldMapper extends WorldMapper {
    @Override
    protected Optional<WorldHolder> getWorld0(UUID uuid) {
        return Optional.ofNullable(MinecraftServer.getInstanceManager().getInstance(uuid))
                .map(MinestomWorldHolder::new);
    }

    @Override
    protected Optional<WorldHolder> getWorld0(String name) {
        final UUID uuid;
        try {
            uuid = UUID.fromString(name);
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
        return Optional.ofNullable(MinecraftServer.getInstanceManager().getInstance(uuid))
                .map(MinestomWorldHolder::new);
    }
}
