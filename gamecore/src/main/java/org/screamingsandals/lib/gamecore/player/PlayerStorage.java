package org.screamingsandals.lib.gamecore.player;

import io.papermc.lib.PaperLib;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;

//Container with player data (inventory, position, etc)
@Data
public class PlayerStorage {
    private Location leftLocation;
    private String displayName;
    private String listName;
    private Collection<PotionEffect> potionEffects;
    private ItemStack[] armor;
    private ItemStack[] inventory;
    private int foodLevel;
    private int level;
    private boolean flying;
    private double health;
    private GameMode gameMode;

    public void restore(Player player, boolean teleport) {
        final var playerInventory = player.getInventory();

        if (teleport) {
            PaperLib.teleportAsync(player, leftLocation);
        }

        player.setDisplayName(displayName);
        player.setPlayerListName(listName);

        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.addPotionEffects(potionEffects);

        playerInventory.setArmorContents(armor);
        playerInventory.setContents(inventory);
        player.setFoodLevel(foodLevel);
        player.setLevel(level);
        player.setHealth(health);
        player.setGameMode(gameMode);

        if (Bukkit.getAllowFlight()) {
            player.setFlying(flying);
        }
    }

    public void store(Player player) {
        final var playerInventory = player.getInventory();

        leftLocation = player.getLocation();
        displayName = player.getDisplayName();
        listName = player.getPlayerListName();
        potionEffects = player.getActivePotionEffects();
        armor = playerInventory.getArmorContents();
        inventory = playerInventory.getContents();
        foodLevel = player.getFoodLevel();
        level = player.getLevel();
        flying = player.isFlying();
        gameMode = player.getGameMode();
        health = player.getHealth();
    }

    public void clean(Player player, boolean spectator) {
        final var playerInventory = player.getInventory();

        playerInventory.setArmorContents(new ItemStack[4]);
        playerInventory.setContents(new ItemStack[]{});

        player.setExp(0.0F);
        player.setLevel(0);
        player.setSneaking(false);
        player.setSprinting(false);
        player.setFoodLevel(20);
        player.setSaturation(10);
        player.setExhaustion(0);
        player.setMaxHealth(20D);
        player.setHealth(player.getMaxHealth());
        player.setFireTicks(0);

        if (player.isInsideVehicle()) {
            player.leaveVehicle();
        }

        for (var effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        if (spectator) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setGameMode(GameMode.SURVIVAL);
        }

        player.updateInventory();
    }
}
