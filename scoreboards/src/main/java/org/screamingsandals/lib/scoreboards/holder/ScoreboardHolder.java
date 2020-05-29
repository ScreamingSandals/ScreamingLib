package org.screamingsandals.lib.scoreboards.holder;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
public class ScoreboardHolder {
    private transient org.bukkit.scoreboard.Scoreboard bukkitScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private String displayedName;
    private DisplaySlot displaySlot;
    private TreeMap<Integer, String> originalLines = new TreeMap<>();

    public TreeMap<Integer, String> getOriginalLines() {
        return new TreeMap<>(originalLines);
    }

    /**
     * Gets lines map with replaced placeholders
     * @param availablePlaceholders map with available placeholders to replace
     * @return map with replaced lines
     */
    public TreeMap<Integer, String> getLines(Map<String, Object> availablePlaceholders) {
        final var toReturn = new TreeMap<Integer, String>();
        for (var entry : originalLines.entrySet()) {
            toReturn.put(entry.getKey(), replace(entry.getValue(), availablePlaceholders));
        }

        return toReturn;
    }

    /**
     * Sorts lines from String list in the order
     * @param fromConfig list of lines from config
     * @return sorted tree map
     */
    public static TreeMap<Integer, String> sortLines(List<String> fromConfig) {
        Collections.reverse(fromConfig);
        final var toReturn = new TreeMap<Integer, String>();

        for (int i = 0; i < fromConfig.size(); i++) {
            var content = fromConfig.get(i);

            if (content.isEmpty()) {
                content = convertToInvisibleString(String.valueOf(i));
            }

            toReturn.put(i, content);
        }

        return toReturn;
    }

    private String replace(String input, Map<String, Object> available) {
        String toReturn = input;

        for (var entry : available.entrySet()) {
            final var entryValue = entry.getValue();
            final var valueToPrint = entryValue != null ? entry.getValue() : "nothing";
            toReturn = toReturn.replaceAll(entry.getKey(), valueToPrint.toString());
        }

        return toReturn;
    }

    private static String convertToInvisibleString(String input) {
        final var hidden = new StringBuilder();
        for (char character : input.toCharArray()) {
            hidden.append(ChatColor.COLOR_CHAR + "").append(character);
        }
        return hidden.toString();
    }

}
