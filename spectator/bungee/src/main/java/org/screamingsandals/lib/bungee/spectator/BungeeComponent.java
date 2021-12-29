package org.screamingsandals.lib.bungee.spectator;

import net.md_5.bungee.api.chat.BaseComponent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.utils.BasicWrapper;

public class BungeeComponent extends BasicWrapper<BaseComponent> implements Component {
    protected BungeeComponent(BaseComponent wrappedObject) {
        super(wrappedObject);
    }
}
