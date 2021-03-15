package org.screamingsandals.lib.scoreboard;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.player.PlayerWrapper;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public abstract class AbstractScoreboard {
    protected TreeMap<Integer, Component> lines = new TreeMap<>();
    protected final List<PlayerWrapper> viewers = new LinkedList<>();
    @Getter
    protected final UUID uuid;

    protected Integer originalLinesSize = 0;

    protected AbstractScoreboard(UUID uuid) {
        this.uuid = uuid;
    }
}
