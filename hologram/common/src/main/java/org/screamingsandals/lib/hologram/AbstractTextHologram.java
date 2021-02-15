package org.screamingsandals.lib.hologram;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.TreeMap;

public abstract class AbstractTextHologram extends AbstractHologram implements TextHologram {
    private TreeMap<Integer, Component> lines = new TreeMap<>();

    protected AbstractTextHologram(LocationHolder location, boolean touchable) {
        super(location, touchable);
    }

    @Override
    public TreeMap<Integer, Component> getLines() {
        return new TreeMap<>(lines);
    }

    @Override
    public TextHologram firstLine(Component text) {
        return newLine(0, text);
    }

    @Override
    public TextHologram newLine(Component text) {
        return newLine(lines.lastKey() + 1, text);
    }

    @Override
    public TextHologram newLine(int line, Component text) {
        //TODO: test!!
        lines = HologramUtils.addEntryAndMoveRest(lines, line, text);
        updateForAll();
        return this;
    }

    @Override
    public TextHologram removeLine(int line) {
        //TODo
        return this;
    }

    @Override
    public TextHologram replaceLines(TreeMap<Integer, Component> lines) {
        this.lines = lines;
        updateForAll();
        return this;
    }
}
