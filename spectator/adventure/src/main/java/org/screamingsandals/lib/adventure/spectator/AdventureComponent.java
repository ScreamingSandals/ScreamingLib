package org.screamingsandals.lib.adventure.spectator;

import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.BasicWrapper;

public class AdventureComponent extends BasicWrapper<net.kyori.adventure.text.Component> implements Component {
    protected AdventureComponent(net.kyori.adventure.text.Component wrappedObject) {
        super(wrappedObject);
    }
}
