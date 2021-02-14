package org.screamingsandals.lib.hologram;

import net.kyori.adventure.text.Component;

import java.util.TreeMap;

public interface TextHologram extends Hologram {

    TreeMap<Integer, Component> getLines();

    void firstLine(Component line);

    void newLine(Component line);

    void newLine(int where, Component line);

    void replaceLines(TreeMap<Integer, Component> map);
}
