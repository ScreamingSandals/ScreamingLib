package org.screamingsandals.lib.minestom.world;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.screamingsandals.lib.utils.annotations.Service;
import org.screamingsandals.lib.world.WorldHolder;
import org.screamingsandals.lib.world.WorldMapper;

import java.util.Optional;
import java.util.UUID;

@Service
public class MinestomWorldMapper extends WorldMapper {
    public MinestomWorldMapper() {
        converter.registerW2P(Instance.class, holder -> MinecraftServer.getInstanceManager().getInstance(holder.getUuid()))
                .registerP2W(Instance.class, MinestomWorldHolder::new);
    }

    @Override
    protected Optional<WorldHolder> getWorld0(UUID uuid) {
        return Optional.ofNullable(MinecraftServer.getInstanceManager().getInstance(uuid))
                .map(MinestomWorldHolder::new);
    }

    @Override
    protected Optional<WorldHolder> getWorld0(String name) {
        try {
            return getWorld0(UUID.fromString(name));
        } catch (IllegalArgumentException ignored) {
            // ignored
        }
        return Optional.empty();
    }
}
