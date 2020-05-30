package org.screamingsandals.lib.bossbars.bossbar;

import lombok.Data;

@Data
public abstract class ScreamingBossbar {
    protected BossbarHolder bossbarHolder;
    protected String identifier;
    protected String originalTitle;

    public ScreamingBossbar() {
        bossbarHolder = new BossbarHolder();
    }
}
