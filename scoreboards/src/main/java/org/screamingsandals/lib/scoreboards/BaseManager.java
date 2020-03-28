package org.screamingsandals.lib.scoreboards;

import lombok.Data;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;

@Data
public abstract class BaseManager<T> {
    public Map<T, Scoreboard> activeScoreboards = new HashMap<>();
    public Map<String, Scoreboard> savedScoreboards = new HashMap<>();

    public void destroy() {
        hideFromAllOwners();

        activeScoreboards.clear();
        savedScoreboards.clear();
    }

    public void showScoreboard(T player, Scoreboard scoreboard) {
    }

    public void hideScoreboard(T player, Scoreboard scoreboard) {

    }

    public void saveScoreboard(String name, Scoreboard scoreboard) {
        savedScoreboards.put(name, scoreboard);
    }

    public void deleteScoreboard(String name) {
        savedScoreboards.remove(name);
    }

    public void showToAllOwners() {

    }

    public void hideFromAllOwners() {

    }

    public void showToOwner(T owner) {

    }

    public void hideFromOwner(T owner) {

    }

}
