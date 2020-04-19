package org.screamingsandals.gamecore.resources;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.screamingsandals.gamecore.GameCore;
import org.screamingsandals.gamecore.core.data.JsonDataSaver;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResourceTypes implements Serializable {
    private final transient File dataFile;
    private final transient JsonDataSaver<ResourceTypes> dataSaver;
    private Map<String, ResourceSpawner.Type> spawnerTypes = new HashMap<>();

    private ResourceTypes() {
        this.dataFile = new File(GameCore.getPlugin().getDataFolder(), "resources.json");
        dataSaver = new JsonDataSaver<>(dataFile, ResourceTypes.class);
    }

    public static ResourceTypes load() {
        ResourceTypes resourceTypes = new ResourceTypes();
        resourceTypes.getDataSaver().load();

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
        ResourceSpawner.Type bronzeType = new ResourceSpawner.Type("Bronze", "TODO", Material.BRICK, ChatColor.DARK_RED, 1);
        ResourceSpawner.Type ironType = new ResourceSpawner.Type("Iron", "TODO", Material.IRON_INGOT, ChatColor.GRAY, 1);
        ResourceSpawner.Type goldType = new ResourceSpawner.Type("Gold", "TODO", Material.GOLD_INGOT, ChatColor.GOLD, 1.2);
        ResourceSpawner.Type diamondType = new ResourceSpawner.Type("Diamond", "TODO", Material.DIAMOND, ChatColor.AQUA, 1.5);

        spawnerTypes = new HashMap<>();
        spawnerTypes.put(bronzeType.getName(), bronzeType);
        spawnerTypes.put(ironType.getName(), ironType);
        spawnerTypes.put(goldType.getName(), goldType);
        spawnerTypes.put(diamondType.getName(), diamondType);
    }

    public void registerType(String name, String translateKey, Material material, ChatColor chatColor, double spread) {
        ResourceSpawner.Type resourceType = new ResourceSpawner.Type(name, translateKey, material, chatColor, spread);
        spawnerTypes.put(name, resourceType);
    }

    public ResourceSpawner.Type getType(String name) {
        return spawnerTypes.get(name);
    }
}

