package org.screamingsandals.lib.hologram;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.TreeMap;
import java.util.UUID;

public abstract class AbstractTextHologram extends AbstractHologram implements TextHologram {
    protected TreeMap<Integer, Component> lines = new TreeMap<>();
    protected Integer originalLinesSize = 0;

    protected AbstractTextHologram(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location, touchable);
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
        if (lines.isEmpty()) {
            return firstLine(text);
        }

        originalLinesSize = lines.size();
        lines.put(lines.lastKey() + 1, text);
        update();
        return this;
    }

    @Override
    public TextHologram newLine(int line, Component text) {
        originalLinesSize = lines.size();
        lines = HologramUtils.addEntryAndMoveRest(lines, line, text);
        update();
        return this;
    }

    @Override
    public TextHologram removeLine(int line) {
        originalLinesSize = lines.size();
        lines = HologramUtils.removeEntryAndMoveRest(lines, line);
        update();
        return this;
    }

    @Override
    public TextHologram replaceLines(TreeMap<Integer, Component> lines) {
        originalLinesSize = lines.size();
        this.lines = lines;
        update();
        return this;
    }

    @Override
    public void destroy() {
        hide();
        lines.clear();
        update();
        viewers.clear();
    }
}
