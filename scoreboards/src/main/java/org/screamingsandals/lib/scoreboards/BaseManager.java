package org.screamingsandals.lib.scoreboards;

import lombok.Data;
import org.screamingsandals.lib.scoreboards.content.Content;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class BaseManager<T> {
    private Map<T, Content> activeScoreboards = new HashMap<>();
    private Map<String, Content> savedScoreboards = new HashMap<>();

    public void destroy() {
        hideAllScoreboards();

        activeScoreboards.clear();
        savedScoreboards.clear();
    }

    public void showScoreboard(T player, Content content) {
        activeScoreboards.remove(player);

        activeScoreboards.put(player, content);
    }

    public void hideScoreboard(T player) {
        activeScoreboards.remove(player);
    }

    public void saveScoreboard(String name, Content content) {
        savedScoreboards.put(name, content);
    }

    public void deleteScoreboard(String name) {
        savedScoreboards.remove(name);
    }

    public void hideAllScoreboards() {

    }
}
