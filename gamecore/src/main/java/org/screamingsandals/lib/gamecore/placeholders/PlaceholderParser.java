package org.screamingsandals.lib.gamecore.placeholders;

import lombok.Data;
import org.screamingsandals.lib.gamecore.core.GameFrame;

import java.util.HashMap;
import java.util.Map;

@Data
public class PlaceholderParser {
    private final GameFrame gameFrame;
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
    }

    public void reload() {
        if (gameFrame == null) {
            return;
        }

        available.clear();
        load();
    }
}
