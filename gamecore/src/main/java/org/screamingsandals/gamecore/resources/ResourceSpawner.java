package org.screamingsandals.gamecore.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.screamingsandals.gamecore.GameCore;
import org.screamingsandals.gamecore.core.adapter.LocationAdapter;
import org.screamingsandals.gamecore.team.GameTeam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public abstract class ResourceSpawner implements Serializable, Runnable {
    //spawner settings
    private LocationAdapter location;
    private Location spawnLocation;
    private int spawnSpeed;
    private int maxSpeed;
    private int maxSpawned;
    private int spawnCount;
    private String name;
    private Type type;
    private Hologram hologram;

    //game stuff
    private GameTeam gameTeam;

    //utils shit
    private transient int remainingToSpawn;
    private transient List<Item> spawnedItems = new ArrayList<>();
    private transient BukkitTask bukkitTask;

    public ResourceSpawner(LocationAdapter location, int spawnSpeed, int maxSpeed,
                           int maxSpawned, GameTeam gameTeam, String name, Type type, Hologram hologram) {
        this.location = location;
        this.spawnLocation = location.getLocation().add(0, 0.05, 0);
        this.spawnSpeed = spawnSpeed;
        this.maxSpeed = maxSpeed;
        this.maxSpawned = maxSpawned;
        this.gameTeam = gameTeam;
        this.name = name;
        this.type = type;
        this.hologram = hologram;
    }

    public void setup() {
        remainingToSpawn = maxSpawned;
    }

    public void start() {
        bukkitTask = Bukkit.getScheduler().runTaskTimer(GameCore.getPlugin(), this, 0L, spawnSpeed);
    }

    public void stop() {
        if (bukkitTask != null && !bukkitTask.isCancelled()) {
            bukkitTask.cancel();
        }
    }

    @Override
    public void run() {
        if (!gameTeam.isAlive() || isMaxSpawned()) {
            stop();
            return;
        }

        spawn();
    }


    private void spawn() {
        final Item item = spawnLocation.getWorld().dropItem(spawnLocation, type.getItemStack(spawnCount));
        final double spread = type.getSpread();

        if (spread != 1.0) {
            item.setVelocity(item.getVelocity().multiply(spread));
        }

        item.setPickupDelay(0);
        spawnedItems.add(item);

        remainingToSpawn = maxSpawned - getSpawnedCount();
    }

    public void setSpawnSpeed(int spawnSpeed) {
        this.spawnSpeed = spawnSpeed;

        stop();
        start();
    }

    public boolean isMaxSpawned() {
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
        private Material material;
        private ChatColor chatColor;
        private double spread;

        public ItemStack getItemStack() {
            return getItemStack(1);
        }

        public ItemStack getItemStack(int amount) {
            final ItemStack itemStack = new ItemStack(material, amount);
            final ItemMeta itemMeta = itemStack.getItemMeta();

            itemMeta.setDisplayName(name);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }

    }

    @Data
    @AllArgsConstructor
    public static class Hologram {
        private boolean enable;
    }
}

