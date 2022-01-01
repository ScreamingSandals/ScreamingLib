package org.screamingsandals.lib.spectator;

import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@LimitedVersionSupport(">= 1.15")
public interface StorageNBTComponent extends NBTComponent {
    NamespacedMappingKey storageKey();

    interface Builder extends NBTComponent.Builder<Builder, StorageNBTComponent> {
        Builder storageKey(NamespacedMappingKey storageKey);
    }
}
