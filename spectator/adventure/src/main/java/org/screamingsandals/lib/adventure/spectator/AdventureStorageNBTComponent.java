/*
 * Copyright 2022 ScreamingSandals
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
