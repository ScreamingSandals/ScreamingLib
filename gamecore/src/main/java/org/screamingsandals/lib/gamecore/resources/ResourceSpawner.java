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
import org.screamingsandals.lib.gamecore.team.GameTeam;
import org.screamingsandals.lib.tasker.BaseTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@EqualsAndHashCode(callSuper = false)
@Data
public class ResourceSpawner implements Serializable {
    //spawner settings
    private LocationAdapter location;
    private int maxSpawned;
    private int amount;
    private int period;
    private TimeUnit timeUnit;
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
        this.timeUnit = type.getTimeUnit();
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
                if (gameTeam != null && !gameTeam.isAlive() || getMaxSpawned()) {
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

        baseTask.runTaskRepeater(1L, period, timeUnit);
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

        remainingToSpawn = maxSpawned - getSpawnedCount();
    }

    public void setPeriod(int period) {
        this.period = period;

        stop();
        start();
    }

    public boolean getMaxSpawned() {
        if (maxSpawned == -1) {
            return false;
        }
        return getSpawnedCount() <= maxSpawned;
    }

    public int getSpawnedCount() {
        return spawnedItems.size();
    }

    public boolean isSame(ResourceSpawner resourceSpawner) {
        return resourceSpawner.getLocation().equals(location)
                && resourceSpawner.getType().getMaterial().equals(type.getMaterial());
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
        private TimeUnit timeUnit;

        public ItemStack getItemStack() {
            return getItemStack(1);
        }

        public ItemStack getItemStack(int amount) {
            final ItemStack itemStack = new ItemStack(material, amount);
            final ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(chatColor + name);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }
}

