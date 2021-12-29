package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@LimitedVersionSupport(">= 1.14")
public interface NBTComponent extends SeparableComponent {
    String nbtPath();

    boolean interpret();

    Target target();

    interface Target {

    }

    interface BlockTarget extends Target {

        // TODO: real position API
        String position();
    }

    interface EntityTarget extends Target {
        String selector();
    }

    @LimitedVersionSupport(">= 1.15")
    interface StorageTarget extends Target {
        NamespacedMappingKey key();
    }
}
