package org.screamingsandals.lib.gamecore.placeholders;

import lombok.Data;
import lombok.ToString;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

import java.util.HashMap;
import java.util.Map;

import static org.screamingsandals.lib.gamecore.language.GameLanguage.m;

@Data
@ToString(exclude = "gameFrame")
public class PlaceholderParser {
    private GameFrame gameFrame;
    private final Map<String, Object> available = new HashMap<>();

    public PlaceholderParser(GameFrame gameFrame) {
        this.gameFrame = gameFrame;

        if (gameFrame == null) {
            return;
        }
        updateBase();
    }

    public void add(String key, Object value) {
        if (key != null && value != null) {
            available.put(key, value);
        }
    }

    public void updateBase() {
        add("%gameName%", gameFrame.getGameName());
        if (gameFrame.getDisplayedName() == null) {
            add("%displayedName%", gameFrame.getGameName());
        } else {
            add("%displayedName%", gameFrame.getDisplayedName());
        }
        add("%maxPlayers%", gameFrame.getMaxPlayers());
        add("%minPlayers%", gameFrame.getMinPlayers());
        add("%startTime%", gameFrame.getStartTime());
        add("%gameTime%", gameFrame.getGameTime());
        add("%deathmatchTime%", gameFrame.getDeathmatchTime());
        add("%endTime%", gameFrame.getEndTime());
        add("%teamsCount%", gameFrame.getTeams().size());
        add("%activeState%", gameFrame.getActiveState());
    }

    public void update() {
        if (gameFrame == null) {
            return;
        }

        updateBase();
        add("%playersToStart%", gameFrame.countRemainingPlayersToStart());
        add("%teamsCount%", gameFrame.getTeams().size());
        add("%playersCount%", gameFrame.getPlayersInGame().size());
        add("%spectatorsCount%", gameFrame.getSpectators().size());
        add("%previousState%", gameFrame.getPreviousState());
        add("%remainingTime%", gameFrame.formatRemainingTime());
        add("%formattedRemainingTime%", gameFrame.formatRemainingTime());
        add("%remainingSeconds%", gameFrame.getRemainingSeconds());
        add("%remainingMinutes%", gameFrame.getRemainingMinutes());
        add("%rawRemainingSeconds%", gameFrame.getRawRemainingSeconds());

        gameFrame.getTeams().forEach(gameTeam -> {
            final var teamName = gameTeam.getName();
            add("%team_" + teamName + "_players%", gameTeam.countPlayersInTeam());
            add("%team_" + teamName + "_color%", gameTeam.getColor());
            add("%team_" + teamName + "_maxPlayers%", gameTeam.getMaxPlayers());
        });
    }

    public void destroy() {
        gameFrame = null;
        available.clear();
    }

    public String parse(String input) {

        if (input == null) {
            return "error";
        }

        String toReturn = input;

        for (var entry : available.entrySet()) {
            final var entryValue = entry.getValue();
            final var valueToPrint = entryValue != null ? entryValue : m("general.null-translated").get();
            toReturn = toReturn.replaceAll(entry.getKey(), valueToPrint.toString());
        }

        return toReturn;
    }

    public String parse(GamePlayer gamePlayer, String input) {
        final var toReturn = parse(input);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            return toReturn;
        }

        return PlaceholderAPI.setPlaceholders(gamePlayer.getPlayer(), toReturn);
    }
}
