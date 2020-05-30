package org.screamingsandals.lib.gamecore.placeholders;

import lombok.Data;
import lombok.ToString;
import org.screamingsandals.lib.gamecore.core.GameFrame;

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

        load();
    }

    public void add(String key, Object value) {
        if (key != null && value != null) {
            available.put(key, value);
        }
    }

    public void load() {
        add("%gameName%", gameFrame.getGameName());
        add("%displayedName%", gameFrame.getDisplayedName());
        add("%maxPlayers%", gameFrame.getMaxPlayers());
        add("%minPlayers%", gameFrame.getMinPlayers());
        add("%minPlayersToStart%", gameFrame.getMinPlayersToStart());
        add("%remainingPlayersToJoin%", gameFrame.countRemainingPlayersToStart());
        add("%lobbyTime%", gameFrame.getLobbyTime());
        add("%startTime%", gameFrame.getStartTime());
        add("%gameTime%", gameFrame.getGameTime());
        add("%deathmatchTime%", gameFrame.getDeathmatchTime());
        add("%endTime%", gameFrame.getEndTime());
        add("%teamsCount%", gameFrame.getTeams().size());
        add("%playersCount%", gameFrame.getPlayersInGame().size());
        add("%spectatorsCount%", gameFrame.getSpectators().size());
        add("%activeState%", gameFrame.getActiveState());
        add("%previousState%", gameFrame.getPreviousState());


        //TODO: teams replacement
    }

    public void destroy() {
        gameFrame = null;
        available.clear();
    }

    public void reload() {
        if (gameFrame == null) {
            return;
        }

        available.clear();
        load();
    }

    public String parse(String input) {
        String toReturn = input;

        for (var entry : available.entrySet()) {
            final var entryValue = entry.getValue();
            final var valueToPrint = entryValue != null ? entry.getValue() : m("general.null-translated").get();
            toReturn = toReturn.replaceAll(entry.getKey(), valueToPrint.toString());
        }

        return toReturn;
    }
}
