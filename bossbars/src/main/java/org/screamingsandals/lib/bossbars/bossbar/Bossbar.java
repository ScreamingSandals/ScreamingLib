package org.screamingsandals.lib.bossbars.bossbar;

import lombok.Data;

import java.io.Serializable;

@Data
public class Bossbar implements Serializable {
    private final BossbarHolder bossbarHolder;
    private String name;

    public Bossbar() {
        bossbarHolder = new BossbarHolder();
    }
}
