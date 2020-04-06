package org.screamingsandals.lib.bossbars.test;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.lib.bossbars.BossbarManager;
import org.screamingsandals.lib.bossbars.bossbar.Bossbar;
import org.screamingsandals.lib.bossbars.bossbar.BossbarCreator;

import java.util.Random;

public class Test extends JavaPlugin implements Listener {
    private BossbarManager bossbarManager;
    private Bossbar bossbar;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        bossbarManager = new BossbarManager();
    }

    @Override
    public void onDisable() {
        bossbarManager.destroy();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String text = "WHATSUP BITCHEEEZ!";

        bossbar = BossbarCreator.get("test", text, BarColor.GREEN, BarStyle.SOLID);
        bossbarManager.saveBossbar(player, bossbar);

        new BukkitRunnable() {
            @Override
            public void run() {
                bossbarManager.showBossbar(player, bossbar);
            }
        }.runTaskLater(this, 2L);

        new BukkitRunnable() {
            @Override
            public void run() {
                BarColor barColor = BarColor.values()[new Random().nextInt(6)];
                BarStyle barStyle = BarStyle.values()[new Random().nextInt(4)];
                ChatColor chatColor = ChatColor.values()[new Random().nextInt(19)];

                bossbar.getBossbarHolder().setBarColor(barColor);
                bossbar.getBossbarHolder().setBarStyle(barStyle);
                bossbar.getBossbarHolder().setTitle(chatColor + text);
            }
        }.runTaskTimer(this, 2L, 5L);
    }
}
