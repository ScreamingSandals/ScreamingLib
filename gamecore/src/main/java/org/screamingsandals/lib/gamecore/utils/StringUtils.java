package org.screamingsandals.lib.gamecore.utils;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    /**
     * @param stack
     * @param input
     */
    public static void addInvisibleString(ItemStack stack, String input) {
        final var itemMeta = stack.getItemMeta();
        List<String> lore = itemMeta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(convertToInvisibleString(input));
        itemMeta.setLore(lore);

        stack.setItemMeta(itemMeta);
    }

    /**
     * @param stack
     * @param startsWith
     * @return
     */
    public static String getFromInvisibleStringThatStartsWith(ItemStack stack, String startsWith) {
        final var itemMeta = stack.getItemMeta();

        if (itemMeta.hasLore()) {
            final List<String> lore = itemMeta.getLore();

            if (lore == null) {
                return null;
            }

            for (String s : lore) {
                final var unhidden = returnFromInvisibleString(s);
                if (unhidden.startsWith(startsWith)) {
                    return unhidden;
                }
            }
        }

        return null;
    }

    /**
     * @param stack
     * @param input
     * @return
     */
    public static boolean isInInvisibleString(ItemStack stack, String input) {
        final var itemMeta = stack.getItemMeta();

        if (itemMeta.hasLore()) {
            final List<String> lore = itemMeta.getLore();

            if (lore == null) {
                return false;
            }

            for (String s : lore) {
                final var unhidden = returnFromInvisibleString(s);
                if (unhidden.equals(input)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static String convertToInvisibleString(String input) {
        final var hidden = new StringBuilder();
        for (char character : input.toCharArray()) {
            hidden.append(ChatColor.COLOR_CHAR + "").append(character);
        }
        return hidden.toString();
    }

    private static String returnFromInvisibleString(String input) {
        return input.replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "");
    }
}
