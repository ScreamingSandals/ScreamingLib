package org.screamingsandals.lib.scoreboards;

import lombok.Data;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class BaseManager<T> {
    private Map<T, Scoreboard> activeScoreboards = new HashMap<>();
    private Map<T, List<Scoreboard>> savedScoreboards = new HashMap<>();

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

    public void saveScoreboard(T player, Scoreboard scoreboard) {
        if (savedScoreboards.containsKey(player)) {
            savedScoreboards.get(player).add(scoreboard);
        } else {
            List<Scoreboard> scoreboards = new ArrayList<>();
            scoreboards.add(scoreboard);

            savedScoreboards.put(player, scoreboards);
        }
    }

    public void deleteSavedScoreboards(T player) {
        savedScoreboards.remove(player);
    }

    public void hideAllScoreboards() {

    }

    public Scoreboard getSavedScoreboard(T player, String name) {
        if (savedScoreboards.containsKey(player)) {
            for (var scoreboard : savedScoreboards.get(player)) {
                if (scoreboard.getName().equalsIgnoreCase(name)) {
                    return scoreboard;
                }
            }
        }
        return null;
    }

    public List<Scoreboard> getSavedScoreboards(T player) {
        final List<Scoreboard> scoreboards = new ArrayList<>();
        if (savedScoreboards.containsKey(player)) {
            scoreboards.addAll(savedScoreboards.get(player));
        }
        return scoreboards;
    }
}
