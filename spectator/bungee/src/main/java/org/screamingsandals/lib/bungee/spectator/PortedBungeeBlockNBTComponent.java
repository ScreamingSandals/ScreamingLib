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

package org.screamingsandals.lib.bungee.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.spectator.backports.BlockNBTPortedComponent;
import org.screamingsandals.lib.spectator.BlockNBTComponent;
import org.screamingsandals.lib.spectator.Component;

public class PortedBungeeBlockNBTComponent extends PortedBungeeNBTComponent implements BlockNBTComponent {
    protected PortedBungeeBlockNBTComponent(@NotNull BlockNBTPortedComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String blockPosition() {
        return ((BlockNBTPortedComponent) wrappedObject).getBlockPosition();
    }

    @Override
    public @NotNull BlockNBTComponent withBlockPosition(@NotNull String blockPosition) {
        var duplicate = ((BlockNBTPortedComponent) wrappedObject).duplicate();
        duplicate.setBlockPosition(blockPosition);
        return (BlockNBTComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public BlockNBTComponent.@NotNull Builder toBuilder() {
        var duplicate = ((BlockNBTPortedComponent) wrappedObject).duplicate();
        return new BungeeBlockNBTBuilder(duplicate);
    }

    @Override
    public @NotNull BlockNBTComponent withInterpret(boolean interpret) {
        return (BlockNBTComponent) super.withInterpret(interpret);
    }

    @Override
    public @NotNull BlockNBTComponent withNbtPath(@NotNull String nbtPath) {
        return (BlockNBTComponent) super.withNbtPath(nbtPath);
    }

    @Override
    public @NotNull BlockNBTComponent withSeparator(@Nullable Component separator) {
        return (BlockNBTComponent) super.withSeparator(separator);
    }

    public static class BungeeBlockNBTBuilder extends BungeeNBTBuilder<BlockNBTComponent, BlockNBTComponent.Builder, BlockNBTPortedComponent> implements BlockNBTComponent.Builder {
        public BungeeBlockNBTBuilder(@NotNull BlockNBTPortedComponent component) {
            super(component);
        }

        @Override
        public BlockNBTComponent.@NotNull Builder blockPosition(@NotNull String blockPosition) {
            component.setBlockPosition(blockPosition);
            return this;
        }
    }
}
