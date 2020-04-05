package org.screamingsandals.lib.bossbars.bossbar;

import lombok.Data;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

@Data
public class BossbarCreator {
    private BossbarHolder bossbarHolder;

    public BossbarCreator() {
        bossbarHolder = new BossbarHolder();
    }

    public BossbarCreator get() {
        return new BossbarCreator();
    }

    public Bossbar get(String name, String title) {
        var bossbarCreator = get();
        var bossbarHolder = bossbarCreator.getBossbarHolder();
        Bossbar bossbar = new Bossbar();

        bossbar.setName(name);
        bossbarHolder.setTitle(title);
        bossbar.setBossbarHolder(bossbarHolder);
        return bossbar;
    }

    public Bossbar get(String name, String title, BarColor barColor, BarStyle barStyle) {
        var bossbarCreator = get();
        var bossbarHolder = bossbarCreator.getBossbarHolder();
        Bossbar bossbar = new Bossbar();

        bossbar.setName(name);
        bossbarHolder.setTitle(title);
        bossbarHolder.setBarColor(barColor);
        bossbarHolder.setBarStyle(barStyle);
        bossbar.setBossbarHolder(bossbarHolder);
        return bossbar;
    }

    public Bossbar get(String name, String title, BarColor barColor, BarStyle barStyle, BarFlag barFlag) {
        var bossbarCreator = get();
        var bossbarHolder = bossbarCreator.getBossbarHolder();
        Bossbar bossbar = new Bossbar();

        bossbar.setName(name);
        bossbarHolder.setTitle(title);
        bossbarHolder.setBarColor(barColor);
        bossbarHolder.setBarStyle(barStyle);
        bossbarHolder.addFlag(barFlag);
        bossbar.setBossbarHolder(bossbarHolder);
        return bossbar;
    }
}
