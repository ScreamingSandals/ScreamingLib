package org.screamingsandals.lib.entity;

import org.screamingsandals.lib.player.PlayerWrapper;

public interface EntityHuman extends EntityLiving {
    PlayerWrapper asPlayer();

    int getExpToLevel();
}
