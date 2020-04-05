package org.screamingsandals.lib.bossbars;

import org.bukkit.entity.Player;
import org.screamingsandals.lib.bossbars.bossbar.Bossbar;

public class BossbarManager extends BaseManager<Player> {

    @Override
    public void showBossbar(Player player, Bossbar bossbar) {
        super.showBossbar(player, bossbar);

        bossbar.getBossbarHolder().getBukkitBossbar().addPlayer(player);
    }

    @Override
    public void hideBossbar(Player player) {
        getActiveBossbars().get(player).getBossbarHolder().getBukkitBossbar().removePlayer(player);

        super.hideBossbar(player);
    }
}
