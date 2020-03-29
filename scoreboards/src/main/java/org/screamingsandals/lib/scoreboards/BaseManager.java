package org.screamingsandals.lib.scoreboards;

import lombok.Data;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class BaseManager<T> {
    private Map<T, Scoreboard> activeScoreboards = new HashMap<>();
    private Map<String, Scoreboard> savedScoreboards = new HashMap<>();

    public void destroy() {
        hideAllScoreboards();

        activeScoreboards.clear();
        savedScoreboards.clear();
    }

    public void showScoreboard(T player, Scoreboard scoreboard) {
        activeScoreboards.remove(player);

        activeScoreboards.put(player, scoreboard);
    }

    public void hideScoreboard(T player) {
        activeScoreboards.remove(player);
    }

    public void saveScoreboard(String name, Scoreboard scoreboard) {
        savedScoreboards.put(name, scoreboard);
    }

    public void deleteScoreboard(String name) {
        savedScoreboards.remove(name);
    }

    public void hideAllScoreboards() {

    }
}
