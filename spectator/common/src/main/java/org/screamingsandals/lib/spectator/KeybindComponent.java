package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;

@LimitedVersionSupport(">= 1.12")
public interface KeybindComponent extends Component {
    String keybind();

    interface Builder extends Component.Builder<Builder, KeybindComponent> {
        Builder keybind(String keybind);
    }
}
