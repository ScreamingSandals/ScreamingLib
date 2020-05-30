package org.screamingsandals.lib.gamecore.visuals.bossbars;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.screamingsandals.lib.gamecore.core.GameFrame;

@EqualsAndHashCode(callSuper = false)
@Data
@ToString(exclude = "gameFrame")
public class BossbarManager {
    private final GameFrame gameFrame;
}
