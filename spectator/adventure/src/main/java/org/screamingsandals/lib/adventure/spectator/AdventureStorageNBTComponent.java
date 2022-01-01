package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.key.Key;
import org.screamingsandals.lib.spectator.StorageNBTComponent;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class AdventureStorageNBTComponent extends AdventureNBTComponent<net.kyori.adventure.text.StorageNBTComponent> implements StorageNBTComponent {
    public AdventureStorageNBTComponent(net.kyori.adventure.text.StorageNBTComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public NamespacedMappingKey storageKey() {
        return NamespacedMappingKey.of(((net.kyori.adventure.text.StorageNBTComponent) wrappedObject).storage().asString());
    }

    public static class AdventureStorageNBTBuilder extends AdventureNBTComponent.AdventureNBTBuilder<
            net.kyori.adventure.text.StorageNBTComponent,
            StorageNBTComponent.Builder,
            StorageNBTComponent,
            net.kyori.adventure.text.StorageNBTComponent.Builder
            > implements StorageNBTComponent.Builder {

        public AdventureStorageNBTBuilder(net.kyori.adventure.text.StorageNBTComponent.Builder builder) {
            super(builder);
        }

        @SuppressWarnings("PatternValidation")
        @Override
        public StorageNBTComponent.Builder storageKey(NamespacedMappingKey storageKey) {
            getBuilder().storage(Key.key(storageKey.toString()));
            return self();
        }
    }
}
