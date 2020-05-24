package org.screamingsandals.lib.gamecore.visuals;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.screamingsandals.lib.bossbars.BaseManager;
import org.screamingsandals.lib.gamecore.core.GameFrame;
import org.screamingsandals.lib.gamecore.player.GamePlayer;

@EqualsAndHashCode(callSuper = false)
@Data
@ToString(exclude = "gameFrame")
public class BossbarManager extends BaseManager<GamePlayer> {
    private final GameFrame gameFrame;
}
