package org.screamingsandals.lib.gamecore.visuals.holograms;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.lib.gamecore.core.GameFrame;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HologramManager extends org.screamingsandals.lib.nms.holograms.HologramManager {
    private final Multimap<GameFrame, GameHologram> gameHolograms = ArrayListMultimap.create();

    public HologramManager(Plugin plugin) {
        super(plugin);
    }

    public GameHologram spawnHologram(GameFrame gameFrame, HologramType type, Player player, Location loc, List<String> lines) {
        return spawnHologram(gameFrame, type, Collections.singletonList(player), loc, lines);
    }

    public GameHologram spawnTouchableHologram(GameFrame gameFrame, HologramType type, Player player, Location loc, List<String> lines) {
        return spawnTouchableHologram(gameFrame, type, Collections.singletonList(player), loc, lines);
    }

    public GameHologram spawnHologram(GameFrame gameFrame, HologramType type, List<Player> players, Location loc, List<String> lines) {
        final var hologram = new GameHologram(gameFrame, type, this, players, loc, lines);
        gameHolograms.put(gameFrame, hologram);

        return hologram;
    }

    public GameHologram spawnTouchableHologram(GameFrame gameFrame, HologramType type, List<Player> players, Location loc, List<String> lines) {
        final var hologram = new GameHologram(gameFrame, type,this, players, loc, lines, true);
        gameHolograms.put(gameFrame, hologram);

        return hologram;
    }


    public void destroyForGame(GameFrame gameFrame) {
        final List<GameHologram> holograms = new LinkedList<>();

        gameHolograms.forEach((foundGame, hologram) -> {
            if (foundGame.getUuid() == gameFrame.getUuid()) {
                holograms.add(hologram);
            }
        });

        holograms.forEach(GameHologram::destroy);
    }

    public void destroy(GameHologram gameHologram) {
        final Iterator<GameHologram> iterator = gameHolograms.values().iterator();

        while (iterator.hasNext()) {
            final var hologram = iterator.next();
            if (hologram.getUuid() == gameHologram.getUuid()) {
                hologram.destroy();
                iterator.remove();
            }
        }
    }

    public void destroy() {
        super.destroy();
        gameHolograms.clear();
    }
}
