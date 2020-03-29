package org.screamingsandals.lib.scoreboards.test;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.screamingsandals.lib.scoreboards.ScoreboardManager;
import org.screamingsandals.lib.scoreboards.content.Content;
import org.screamingsandals.lib.scoreboards.scoreboard.Scoreboard;
import org.screamingsandals.lib.scoreboards.scoreboard.ScoreboardCreator;

import java.util.ArrayList;
import java.util.List;

public class Test extends JavaPlugin implements Listener {
    public static ScoreboardManager scoreboardManager;
    public Scoreboard scoreboard;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        scoreboardManager = new ScoreboardManager();

        List<String> list = new ArrayList<>();
        list.add("Blbost1");
        list.add("Blbost5");
        list.add("Blbost3");
        list.add("Blbost6");
        scoreboard = ScoreboardCreator.get("Blbost").create("Kokotina!", DisplaySlot.SIDEBAR, Content.sortLines(list));

        scoreboardManager.saveScoreboard(scoreboard.getScoreboardName(), scoreboard);
    }

    @Override
    public void onDisable() {
        scoreboardManager.destroy();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                scoreboardManager.showScoreboard(event.getPlayer(), scoreboard);
            }
        }.runTaskLater(this, 2L);
    }
}
