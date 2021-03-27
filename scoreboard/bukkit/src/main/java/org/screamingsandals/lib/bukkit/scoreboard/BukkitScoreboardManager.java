package org.screamingsandals.lib.bukkit.scoreboard;

import org.screamingsandals.lib.scoreboard.Scoreboard;
import org.screamingsandals.lib.scoreboard.ScoreboardManager;
import org.screamingsandals.lib.utils.Controllable;
import org.screamingsandals.lib.utils.annotations.Service;

import java.util.UUID;

@Service
public class BukkitScoreboardManager extends ScoreboardManager {

    public static void init(Controllable controllable) {
        ScoreboardManager.init(() -> new BukkitScoreboardManager(controllable));
    }

    protected BukkitScoreboardManager(Controllable controllable) {
        super(controllable);
    }

    @Override
    protected Scoreboard scoreboard0(UUID uuid) {
        return new BukkitScoreboard(uuid);
    }
}
