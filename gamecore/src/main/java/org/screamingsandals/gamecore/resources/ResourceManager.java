package org.screamingsandals.gamecore.resources;

import lombok.Data;
import org.screamingsandals.gamecore.core.GameFrame;
import org.screamingsandals.gamecore.upgrades.UpgradeManager;

import java.util.List;

@Data
public abstract class ResourceManager {
    private final GameFrame gameFrame;
    private final ResourceTypes resourceTypes;
    private final UpgradeManager upgradeManager;
    private final List<ResourceSpawner> resourceSpawners;

    public ResourceManager(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        resourceTypes = gameFrame.getResourceTypes();
        upgradeManager = new UpgradeManager();
        resourceSpawners = gameFrame.getSpawners();
    }

    public void start() {
        resourceSpawners.forEach(ResourceSpawner::setup);
        resourceSpawners.forEach(ResourceSpawner::start);
    }

    public void stop() {
        resourceSpawners.forEach(ResourceSpawner::stop);
    }
}
