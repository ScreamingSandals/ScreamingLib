package org.screamingsandals.lib.spectator;

import java.util.Collection;
import java.util.List;

public interface TranslatableComponent extends Component {
    String translate();

    List<Component> args();

    interface Builder extends Component.Builder<Builder, TranslatableComponent> {
        Builder translate(String translate);

        Builder args(Component...components);

        Builder args(Collection<Component> components);
    }
}
