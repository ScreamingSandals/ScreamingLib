/*
 * Copyright 2024 ScreamingSandals
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

package org.screamingsandals.lib.impl.bungee.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bungee.spectator.backports.StorageNBTPortedComponent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.StorageNBTComponent;
import org.screamingsandals.lib.utils.ResourceLocation;

public class PortedBungeeStorageNBTComponent extends PortedBungeeNBTComponent implements StorageNBTComponent {
    protected PortedBungeeStorageNBTComponent(@NotNull StorageNBTPortedComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull ResourceLocation storageKey() {
        return ResourceLocation.of(((StorageNBTPortedComponent) wrappedObject).getStorageKey());
    }

    @Override
    public @NotNull StorageNBTComponent withStorageKey(@NotNull ResourceLocation storageKey) {
        var duplicate = ((StorageNBTPortedComponent) wrappedObject).duplicate();
        duplicate.setStorageKey(storageKey.asString());
        return (StorageNBTComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public StorageNBTComponent.@NotNull Builder toBuilder() {
        var duplicate = ((StorageNBTPortedComponent) wrappedObject).duplicate();
        return new BungeeStorageNBTBuilder(duplicate);
    }

    @Override
    public @NotNull StorageNBTComponent withInterpret(boolean interpret) {
        return (StorageNBTComponent) super.withInterpret(interpret);
    }

    @Override
    public @NotNull StorageNBTComponent withNbtPath(@NotNull String nbtPath) {
        return (StorageNBTComponent) super.withNbtPath(nbtPath);
    }

    @Override
    public @NotNull StorageNBTComponent withSeparator(@Nullable Component separator) {
        return (StorageNBTComponent) super.withSeparator(separator);
    }

    public static class BungeeStorageNBTBuilder extends BungeeNBTBuilder<StorageNBTComponent, StorageNBTComponent.Builder, StorageNBTPortedComponent> implements StorageNBTComponent.Builder {
        public BungeeStorageNBTBuilder(@NotNull StorageNBTPortedComponent component) {
            super(component);
        }

        @Override
        public StorageNBTComponent.@NotNull Builder storageKey(@NotNull ResourceLocation storageKey) {
            component.setStorageKey(storageKey.asString());
            return this;
        }
    }
}
