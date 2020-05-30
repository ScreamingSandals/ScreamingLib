package org.screamingsandals.lib.gamecore.upgrades.base;

import java.util.UUID;

public interface Upgradable {

    UUID getUuid();

    UpgradeType getType();

    int getDuration();

    boolean isActive();
}
