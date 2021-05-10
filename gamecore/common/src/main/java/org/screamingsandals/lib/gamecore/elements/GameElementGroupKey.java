package org.screamingsandals.lib.gamecore.elements;

import lombok.Data;

@Data
public class GameElementGroupKey<E extends GameElement> {
    private final Class<E> type;
    private final String key;
    private final boolean persistent;
}
