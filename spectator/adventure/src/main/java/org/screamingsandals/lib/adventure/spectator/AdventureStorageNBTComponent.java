package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.text.StorageNBTComponent;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class AdventureStorageNBTComponent extends AdventureNBTComponent<StorageNBTComponent> {
    public AdventureStorageNBTComponent(StorageNBTComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public Target target() {
        return (StorageTarget) () -> NamespacedMappingKey.of(((StorageNBTComponent) wrappedObject).storage().asString());
    }
}
