package org.screamingsandals.lib.gamecore.upgrades.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public abstract class BaseUpgrade implements Upgradable {
    private final UUID uuid = UUID.randomUUID();
    protected UpgradeType upgradeType;
    protected int duration;
}
