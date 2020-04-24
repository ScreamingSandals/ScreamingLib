package org.screamingsandals.gamecore.resources;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.screamingsandals.gamecore.core.GameFrame;
import org.screamingsandals.gamecore.core.data.file.JsonDataSource;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceTypes implements Serializable, Cloneable {
    private final transient GameFrame gameFrame;
    private transient File dataFile;
    private transient JsonDataSource<ResourceTypes> dataSaver;
    private Map<String, ResourceSpawner.Type> spawnerTypes = new HashMap<>();

    public static ResourceTypes load(GameFrame gameFrame) {
        ResourceTypes resourceTypes = new ResourceTypes(gameFrame);
        resourceTypes = resourceTypes.getDataSaver().load();

        if (resourceTypes.getSpawnerTypes() == null) {
            resourceTypes.createDefaultTypes();
            resourceTypes.save();
        }

        return resourceTypes;
    }

    public void save() {
        dataSaver.save(this);
    }

    public void createDefaultTypes() {
        ResourceSpawner.Type bronzeType = new ResourceSpawner.Type("Bronze", Material.BRICK, ChatColor.DARK_RED, 1);
        ResourceSpawner.Type ironType = new ResourceSpawner.Type("Iron", Material.IRON_INGOT, ChatColor.GRAY, 1);
        ResourceSpawner.Type goldType = new ResourceSpawner.Type("Gold", Material.GOLD_INGOT, ChatColor.GOLD, 1.2);
        ResourceSpawner.Type diamondType = new ResourceSpawner.Type("Diamond", Material.DIAMOND, ChatColor.AQUA, 1.5);

        spawnerTypes = new HashMap<>();
        spawnerTypes.put(bronzeType.getName(), bronzeType);
        spawnerTypes.put(ironType.getName(), ironType);
        spawnerTypes.put(goldType.getName(), goldType);
        spawnerTypes.put(diamondType.getName(), diamondType);
    }

    public void registerType(String name, Material material, ChatColor chatColor, double spread) {
        ResourceSpawner.Type resourceType = new ResourceSpawner.Type(name, material, chatColor, spread);
        spawnerTypes.put(name, resourceType);
    }

    public void registerType(ResourceSpawner.Type type) {
        spawnerTypes.put(type.getName(), type);
    }

    public ResourceSpawner.Type getType(String name) {
        return spawnerTypes.get(name);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

