package org.screamingsandals.lib.gamecore.resources;

import lombok.Data;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.core.GTimeUnit;
import org.screamingsandals.lib.gamecore.core.data.file.JsonDataSource;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@ToString(exclude = {"gameFrame"})
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
        var toReturn = dataSaver.load();

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
        ResourceSpawner.Type bronzeType = new ResourceSpawner.Type("Bronze", "core.materials.bronze", Material.BRICK, ChatColor.DARK_RED, 1, 1, 1, GTimeUnit.SECONDS);
        ResourceSpawner.Type ironType = new ResourceSpawner.Type("Iron", "core.materials.iron", Material.IRON_INGOT, ChatColor.GRAY, 1, 1, 10, GTimeUnit.SECONDS);
        ResourceSpawner.Type goldType = new ResourceSpawner.Type("Gold", "core.materials.gold", Material.GOLD_INGOT, ChatColor.GOLD, 1.1, 1, 24, GTimeUnit.SECONDS);
        ResourceSpawner.Type diamondType = new ResourceSpawner.Type("Diamond", "core.materials.diamond", Material.DIAMOND, ChatColor.AQUA, 1.3, 1, 50, GTimeUnit.SECONDS);

        spawnerTypes = new HashMap<>();
        spawnerTypes.put(bronzeType.getName(), bronzeType);
        spawnerTypes.put(ironType.getName(), ironType);
        spawnerTypes.put(goldType.getName(), goldType);
        spawnerTypes.put(diamondType.getName(), diamondType);
    }

    public void registerType(String name, String translateKey, Material material, ChatColor chatColor,
                             double spread, int amount, int period, GTimeUnit gTimeUnit) {
        ResourceSpawner.Type resourceType = new ResourceSpawner.Type(name, translateKey, material, chatColor, spread, amount, period, gTimeUnit);
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

