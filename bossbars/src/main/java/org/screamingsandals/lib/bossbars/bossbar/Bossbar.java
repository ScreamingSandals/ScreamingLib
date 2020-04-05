package org.screamingsandals.lib.bossbars.bossbar;

import lombok.Data;

import java.io.Serializable;

@Data
public class Bossbar implements Serializable {
    private String name;
    private BossbarHolder bossbarHolder;
}
