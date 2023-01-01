/*
 * Copyright 2023 ScreamingSandals
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.StorageNBTComponent;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

public class AdventureStorageNBTComponent extends AdventureNBTComponent<net.kyori.adventure.text.StorageNBTComponent> implements StorageNBTComponent {
    public AdventureStorageNBTComponent(net.kyori.adventure.text.@NotNull StorageNBTComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    @NotNull
    public NamespacedMappingKey storageKey() {
        return NamespacedMappingKey.of(((net.kyori.adventure.text.StorageNBTComponent) wrappedObject).storage().asString());
    }

    @SuppressWarnings("PatternValidation")
    @Override
    @NotNull
    public StorageNBTComponent withStorageKey(@NotNull NamespacedMappingKey storageKey) {
        return (StorageNBTComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.StorageNBTComponent) wrappedObject).storage(Key.key(storageKey.asString())));
    }

    @Override
    @NotNull
    public StorageNBTComponent.Builder toBuilder() {
        return new AdventureStorageNBTBuilder(((net.kyori.adventure.text.StorageNBTComponent) wrappedObject).toBuilder());
    }

    @Override
    @NotNull
    public StorageNBTComponent withNbtPath(@NotNull String nbtPath) {
        return (StorageNBTComponent) super.withNbtPath(nbtPath);
    }

    @Override
    @NotNull
    public StorageNBTComponent withInterpret(boolean interpret) {
        return (StorageNBTComponent) super.withInterpret(interpret);
    }

    @Override
    @NotNull
    public StorageNBTComponent withSeparator(@Nullable Component separator) {
        return (StorageNBTComponent) super.withSeparator(separator);
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
        @NotNull
        public StorageNBTComponent.Builder storageKey(@NotNull NamespacedMappingKey storageKey) {
            getBuilder().storage(Key.key(storageKey.asString()));
            return self();
        }
    }
}
