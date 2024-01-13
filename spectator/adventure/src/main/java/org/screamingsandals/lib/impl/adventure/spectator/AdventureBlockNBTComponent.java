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

package org.screamingsandals.lib.impl.adventure.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.BlockNBTComponent;
import org.screamingsandals.lib.spectator.Component;

public class AdventureBlockNBTComponent extends AdventureNBTComponent<net.kyori.adventure.text.BlockNBTComponent> implements BlockNBTComponent {
    public AdventureBlockNBTComponent(net.kyori.adventure.text.@NotNull BlockNBTComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String blockPosition() {
        return ((net.kyori.adventure.text.BlockNBTComponent) wrappedObject).pos().asString();
    }

    @Override
    public @NotNull BlockNBTComponent withBlockPosition(@NotNull String blockPosition) {
        return (BlockNBTComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.BlockNBTComponent) wrappedObject).pos(net.kyori.adventure.text.BlockNBTComponent.Pos.fromString(blockPosition)));
    }

    @Override
    public BlockNBTComponent.@NotNull Builder toBuilder() {
        return new AdventureBlockNBTBuilder(((net.kyori.adventure.text.BlockNBTComponent) wrappedObject).toBuilder());
    }

    @Override
    public @NotNull BlockNBTComponent withNbtPath(@NotNull String nbtPath) {
        return (BlockNBTComponent) super.withNbtPath(nbtPath);
    }

    @Override
    public @NotNull BlockNBTComponent withInterpret(boolean interpret) {
        return (BlockNBTComponent) super.withInterpret(interpret);
    }

    @Override
    public @NotNull BlockNBTComponent withSeparator(@Nullable Component separator) {
        return (BlockNBTComponent) super.withSeparator(separator);
    }

    public static class AdventureBlockNBTBuilder extends AdventureNBTComponent.AdventureNBTBuilder<
            net.kyori.adventure.text.BlockNBTComponent,
            BlockNBTComponent.Builder,
            BlockNBTComponent,
            net.kyori.adventure.text.BlockNBTComponent.Builder
            > implements BlockNBTComponent.Builder {

        public AdventureBlockNBTBuilder(net.kyori.adventure.text.BlockNBTComponent.Builder builder) {
            super(builder);
        }

        @Override
        public BlockNBTComponent.@NotNull Builder blockPosition(@NotNull String blockPosition) {
            getBuilder().pos(net.kyori.adventure.text.BlockNBTComponent.Pos.fromString(blockPosition));
            return self();
        }
    }
}
