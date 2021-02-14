package org.screamingsandals.lib.hologram.builder;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.hologram.HologramCreator;
import org.screamingsandals.lib.hologram.HologramUtils;
import org.screamingsandals.lib.hologram.TextHologram;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.TreeMap;

@Getter
public class TextHologramBuilder extends AbstractHologramBuilder {
    private final TreeMap<Integer, Component> lines = new TreeMap<>();

    public static TextHologramBuilder newBuilder() {
        return new TextHologramBuilder();
    }

    public static TextHologramBuilder newBuilder(LocationHolder location) {
        final var toReturn = new TextHologramBuilder();
        toReturn.location = location;
        return toReturn;
    }

    public TextHologramBuilder line(int line, Component text) {
        HologramUtils.addEntryAndMoveRest(lines, line, text);
        return this;
    }

    public TextHologramBuilder firstLine(Component text) {
        return line(0, text);
    }

    public TextHologramBuilder line(Component text) {
        return line(lines.lastKey() + 1, text);
    }

    public TextHologramBuilder viewer(PlayerWrapper player) {
        viewers.add(player);
        return this;
    }

    public TextHologramBuilder viewer(List<PlayerWrapper> players) {
        viewers.addAll(players);
        return this;
    }

    public TextHologramBuilder touch(Hologram.TouchHandler handler) {
        touchHandlers.add(handler);
        return this;
    }

    public TextHologramBuilder touch(List<Hologram.TouchHandler> handlers) {
        touchHandlers.addAll(handlers);
        return this;
    }

    public TextHologramBuilder location(LocationHolder location) {
        this.location = location;
        return this;
    }

    public TextHologramBuilder viewDistance(int newViewDistance) {
        this.viewDistance = newViewDistance;
        return this;
    }

    public TextHologram build() {
        return HologramCreator.buildTextHologram(this);
    }


}
