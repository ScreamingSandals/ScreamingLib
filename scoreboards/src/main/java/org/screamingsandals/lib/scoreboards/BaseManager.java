package org.screamingsandals.lib.scoreboards;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class BaseManager<T> {
    private Map<T, Scoreboard> activeScoreboards = new HashMap<>();
    private Multimap<T, Scoreboard> savedScoreboards = ArrayListMultimap.create();

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
        savedScoreboards.put(player, scoreboard);
    }

    public void deleteSavedScoreboards(T player) {
        savedScoreboards.removeAll(player);
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
