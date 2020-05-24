package org.screamingsandals.lib.bossbars;

import lombok.Data;
import org.screamingsandals.lib.bossbars.bossbar.Bossbar;

import java.util.*;

@Data
public abstract class BaseManager<T> {
    private Map<T, Bossbar> activeBossbars = new HashMap<>();
    private Map<T, List<Bossbar>> savedBossbars = new HashMap<>();

    public void destroy() {
        hideAllBossbars();

        activeBossbars.clear();
        savedBossbars.clear();
    }

    public void showBossbar(T player, Bossbar bossbar) {
        if (activeBossbars.containsKey(player)) {
            activeBossbars.get(player).getBossbarHolder().setVisible(false);
            activeBossbars.remove(player);
        }

        activeBossbars.put(player, bossbar);
    }

    public void hideBossbar(T player) {
        activeBossbars.remove(player);
    }

    public void saveBossbar(T player, Bossbar bossbar) {
        if (savedBossbars.containsKey(player)) {
            savedBossbars.get(player).add(bossbar);
        } else {
            List<Bossbar> bossbars = new LinkedList<>();
            bossbars.add(bossbar);

            savedBossbars.put(player, bossbars);
        }
    }

    public void deleteSavedBossbars(T player) {
        savedBossbars.remove(player);
    }

    public void hideAllBossbars() {

    }

    public Bossbar getBossbar(T player, String name) {
        if (savedBossbars.containsKey(player)) {
            for (var bossbar : savedBossbars.get(player)) {
                if (bossbar.getName().equalsIgnoreCase(name)) {
                    return bossbar;
                }
            }
        }
        return null;
    }

    public List<Bossbar> getSavedBossbars(T player) {
        final List<Bossbar> bossbars = new LinkedList<>();
        if (savedBossbars.containsKey(player)) {
            bossbars.addAll(savedBossbars.get(player));
        }
        return bossbars;
    }
}
