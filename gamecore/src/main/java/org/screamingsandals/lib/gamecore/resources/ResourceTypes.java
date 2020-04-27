package org.screamingsandals.lib.gamecore.resources;

import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.data.file.JsonDataSource;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResourceTypes implements Serializable, Cloneable {
    private transient GameFrame gameFrame;
    private transient JsonDataSource<ResourceTypes> dataSaver;
    private Map<String, ResourceSpawner.Type> spawnerTypes = new HashMap<>();

    public ResourceTypes(GameFrame gameFrame, File file) {
        this.gameFrame = gameFrame;
        this.dataSaver = new JsonDataSource<>(file, ResourceTypes.class);
    }

    public static ResourceTypes load(GameFrame gameFrame, File file) {
        final JsonDataSource<ResourceTypes> dataSaver = new JsonDataSource<>(file, ResourceTypes.class);
        ResourceTypes toReturn = dataSaver.load();

        if (toReturn == null) {
            toReturn = new ResourceTypes(gameFrame, file);
            toReturn.createDefaultTypes();
            toReturn.save();
        }

        toReturn.setDataSaver(dataSaver);
        toReturn.setGameFrame(gameFrame);
        return toReturn;
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

