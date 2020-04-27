package org.screamingsandals.lib.gamecore.team;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum TeamColor {
    BLACK(ChatColor.BLACK, "BLACK", Color.BLACK),
    BLUE(ChatColor.DARK_BLUE, "BLUE", Color.fromRGB(0, 0, 170)),
    GREEN(ChatColor.DARK_GREEN, "GREEN", Color.fromRGB(0, 170, 0)),
    RED(ChatColor.RED, "RED", Color.fromRGB(255, 85, 85)),
    MAGENTA(ChatColor.DARK_PURPLE, "MAGENTA", Color.fromRGB(170, 0, 170)),
    ORANGE(ChatColor.GOLD, "ORANGE", Color.fromRGB(255, 170, 0)),
    LIGHT_GRAY(ChatColor.GRAY, "LIGHT_GRAY", Color.fromRGB(170, 170, 170)),
    GRAY(ChatColor.DARK_GRAY, "GRAY", Color.fromRGB(85, 85, 85)),
    LIGHT_BLUE(ChatColor.BLUE, "LIGHT_BLUE", Color.fromRGB(85, 85, 255)),
    LIME(ChatColor.GREEN, "LIME", Color.fromRGB(85, 255, 85)),
    CYAN(ChatColor.AQUA, "CYAN", Color.fromRGB(85, 255, 255)),
    PINK(ChatColor.LIGHT_PURPLE, "PINK", Color.fromRGB(255, 85, 255)),
    YELLOW(ChatColor.YELLOW, "YELLOW", Color.fromRGB(255, 255, 85)),
    WHITE(ChatColor.WHITE, "WHITE", Color.WHITE),
    BROWN(ChatColor.DARK_RED, "BROWN", Color.fromRGB(139, 69, 19));

    public final ChatColor chatColor;
    public final String colorName;
    public final Color leatherColor;

    TeamColor(ChatColor chatColor, String colorName, Color leatherColor) {
        this.chatColor = chatColor;
        this.colorName = colorName;
        this.leatherColor = leatherColor;
    }

    public ItemStack getWool() {
        return new ItemStack(Material.valueOf(colorName + "_WOOL"));
    }
}
