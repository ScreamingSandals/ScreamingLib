package org.screamingsandals.lib.gamecore.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.lib.gamecore.adapter.LocationAdapter;
import org.screamingsandals.lib.gamecore.core.GameTimeUnit;
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.gamecore.utils.StringUtils;
import org.screamingsandals.lib.tasker.BaseTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;

@EqualsAndHashCode(callSuper = false)
@Data
public class ResourceSpawner implements Serializable, Cloneable {
    //spawner settings
    private LocationAdapter location;
    private int maxSpawned;
    private int amount;
    private int period;
    private GameTimeUnit gameTimeUnit;
    private UUID uuid;
    private Type type;
    private boolean hologram;

    //game stuff
    private GameTeam gameTeam;

    //utils shit
    private transient Location spawnLocation;
    private transient int remainingToSpawn;
    private transient List<Item> spawnedItems = new ArrayList<>();
    private transient BaseTask baseTask;

    public ResourceSpawner(LocationAdapter location, Type type) {
        this(location, type, null, true, -1);
    }

    public ResourceSpawner(LocationAdapter location, Type type, boolean hologram) {
        this(location, type, null, hologram, -1);
    }

    public ResourceSpawner(LocationAdapter location, Type type, GameTeam gameTeam, boolean hologram, int maxSpawned) {
        this.location = location;
        this.type = type;
        this.amount = type.getAmount();
        this.period = type.getPeriod();
        this.gameTimeUnit = type.getGameTimeUnit();
        this.maxSpawned = maxSpawned;
        this.gameTeam = gameTeam;
        this.uuid = UUID.randomUUID();
        this.hologram = hologram;
    }

    public void setup() {
        this.spawnLocation = location.getLocation().add(0, 0.05, 0);
        remainingToSpawn = maxSpawned;
    }

    public void start() {
        setup();

        baseTask = new BaseTask() {
            @Override
            public void run() {
                if (gameTeam != null && !gameTeam.isAlive()) {
                    stop();
                }

                if (isMaxSpawned()) {
                    stop();
                    return;
                }
                try {
                    spawn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        baseTask.runTaskRepeater(1L, GameTimeUnit.getTimeUnitValue(period, gameTimeUnit), gameTimeUnit.getTimeUnit());

    }

    public void stop() {
        if (baseTask != null && !baseTask.hasStopped()) {
            baseTask.stop();
        }
    }

    private void spawn() {
        final Item item = spawnLocation.getWorld().dropItem(spawnLocation, type.getItemStack(amount));
        final double spread = type.getSpread();

        if (spread != 1.0) {
            item.setVelocity(item.getVelocity().multiply(spread));
        }

        item.setPickupDelay(0);
        spawnedItems.add(item);

        //TODO: calculate next spawn
        remainingToSpawn = maxSpawned - getSpawnedCount();
    }

    public void changePeriod(int period) {
        this.period = period;

        stop();
        start();
    }

    public boolean isMaxSpawned() {
        if (maxSpawned == -1) {
            return false;
        }
        return getSpawnedCount() < maxSpawned;
    }

    public int getSpawnedCount() {
        return spawnedItems.size();
    }

    public boolean isSame(ResourceSpawner resourceSpawner) {
        return resourceSpawner.getLocation().equals(location)
                && resourceSpawner.getType().getMaterial().equals(type.getMaterial());
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Data
    @AllArgsConstructor
    public static class Type {
        private String name;
        private String translateKey;
        private Material material;
        private ChatColor chatColor;
        private double spread;
        private int amount;
        private int period;
        private GameTimeUnit gameTimeUnit;

        public String getTranslatedName() {
            if (translateKey != null) {
                final var translatedName = m(translateKey).get();

                if (!translatedName.equalsIgnoreCase(translateKey)) {
                    return translatedName;
                }
            }
            return chatColor + name;
        }

        public ItemStack getItemStack() {
            return getItemStack(1);
        }

        public ItemStack getItemStack(int amount) {
            final ItemStack itemStack = new ItemStack(material, amount);
            final ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(getTranslatedName());
            itemStack.setItemMeta(itemMeta);

            StringUtils.addInvisibleString(itemStack, name);

            return itemStack;
        }

        public boolean isSame(ItemStack itemStack) {
            return itemStack.getType() == material && StringUtils.isInInvisibleString(itemStack, name);
        }
    }
}

