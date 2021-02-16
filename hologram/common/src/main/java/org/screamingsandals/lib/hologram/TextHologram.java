package org.screamingsandals.lib.hologram;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.TreeMap;

public interface TextHologram extends Hologram {

    TreeMap<Integer, Component> getLines();

    TextHologram firstLine(Component text);

    TextHologram newLine(List<Component> text);

    TextHologram newLine(Component text);

    TextHologram newLine(int line, Component text);

    TextHologram removeLine(int line);

    TextHologram replaceLines(TreeMap<Integer, Component> lines);
}
