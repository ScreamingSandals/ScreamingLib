package org.screamingsandals.lib.gamecore.resources;

import lombok.Data;
import lombok.ToString;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.upgrades.UpgradeManager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@ToString(exclude = "gameFrame")
public class ResourceManager implements Serializable {
    private final transient UpgradeManager upgradeManager = new UpgradeManager();
    private final List<ResourceSpawner> resourceSpawners = new LinkedList<>();
    private transient GameFrame gameFrame;
    private ResourceTypes resourceTypes;

    public ResourceManager(GameFrame gameFrame) {
        prepare(gameFrame);
    }

    public void prepare(GameFrame gameFrame) {
        if (this.gameFrame == null) {
            this.gameFrame = gameFrame;
        }
    }

    public void start() {
        resourceSpawners.forEach(ResourceSpawner::start);
    }

    public void stop() {
        resourceSpawners.forEach(ResourceSpawner::stop);
    }

    public void register(ResourceSpawner resourceSpawner) {
        resourceSpawners.add(resourceSpawner);
    }

    public void unregister(ResourceSpawner resourceSpawner) {
        resourceSpawners.remove(resourceSpawner);
    }

    public void unregister(UUID uuid) {
        final var toUnregister = getByID(uuid);
        toUnregister.ifPresent(resourceSpawners::remove);
    }

    public boolean isRegistered(ResourceSpawner resourceSpawner) {
        return isRegistered(resourceSpawner.getUuid());
    }

    public boolean isRegistered(UUID uuid) {
        return getByID(uuid).isPresent();
    }

    public Optional<ResourceSpawner> getByID(UUID uuid) {
        for (var spawner : resourceSpawners) {
            if (spawner.getUuid() == uuid) {
                return Optional.of(spawner);
            }
        }

        return Optional.empty();
    }
}
