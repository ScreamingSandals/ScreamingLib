package org.screamingsandals.lib.gamecore.visuals;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.player.GamePlayer;
import org.screamingsandals.lib.bossbars.BaseManager;

@EqualsAndHashCode(callSuper = false)
@Data
public class BossbarManager extends BaseManager<GamePlayer> {
    private final GameFrame gameFrame;
}
