package org.screamingsandals.lib.spectator;

import java.util.List;

public interface TranslatableComponent extends Component {
    String translate();

    List<Component> args();
}
