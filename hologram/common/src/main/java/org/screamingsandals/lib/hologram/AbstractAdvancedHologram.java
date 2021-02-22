package org.screamingsandals.lib.hologram;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.material.Item;
import org.screamingsandals.lib.tasker.TaskerTime;
import org.screamingsandals.lib.utils.Pair;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.List;
import java.util.TreeMap;
import java.util.UUID;


public abstract class AbstractAdvancedHologram extends AbstractHologram implements AdvancedHologram {
    protected TreeMap<Integer, Component> lines = new TreeMap<>();
    protected Integer originalLinesSize = 0;
    protected Pair<Integer, TaskerTime> rotatingTime;
    protected boolean isRotating;
    protected Item item;

    protected AbstractAdvancedHologram(UUID uuid, LocationHolder location, boolean touchable) {
        super(uuid, location, touchable);
    }

    @Override
    public TreeMap<Integer, Component> getLines() {
        return new TreeMap<>(lines);
    }

    @Override
    public TextHologram firstLine(Component text) {
        return setLine(0, text);
    }

    @Override
    public TextHologram newLine(List<Component> text) {
        text.forEach(this::newLine);
        return this;
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
    public TextHologram setLine(int line, Component text) {
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

    @Override
    public ItemHologram rotating(boolean isRotating) {
        this.isRotating = isRotating;
        update();
        return this;
    }

    @Override
    public boolean isRotating() {
        return isRotating;
    }

    @Override
    public Pair<Integer, TaskerTime> getRotatingTime() {
        return rotatingTime;
    }

    @Override
    public void setRotatingTime(Pair<Integer, TaskerTime> rotatingTime) {
        this.rotatingTime = rotatingTime;
    }

    @Override
    public ItemHologram item(Item item) {
        this.item = item;
        update();
        return this;
    }
}
