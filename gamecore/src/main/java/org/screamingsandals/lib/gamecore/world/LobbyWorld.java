package org.screamingsandals.lib.gamecore.world;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class LobbyWorld extends BaseWorld {

    public static GameWorld get() {
        return new GameWorld();
    }
}
