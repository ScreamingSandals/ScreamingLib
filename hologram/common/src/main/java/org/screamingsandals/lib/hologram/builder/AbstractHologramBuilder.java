package org.screamingsandals.lib.hologram.builder;

import lombok.Getter;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

import java.util.LinkedList;
import java.util.List;

@Getter
public abstract class AbstractHologramBuilder {
    protected final List<PlayerWrapper> viewers = new LinkedList<>();
    protected final List<Hologram.TouchHandler> touchHandlers = new LinkedList<>();
    protected LocationHolder location;
    protected int viewDistance = 4096;
}
