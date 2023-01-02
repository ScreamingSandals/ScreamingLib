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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.EntityNBTComponent;

public class AdventureEntityNBTComponent extends AdventureNBTComponent<net.kyori.adventure.text.EntityNBTComponent> implements EntityNBTComponent {
    public AdventureEntityNBTComponent(net.kyori.adventure.text.@NotNull EntityNBTComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String selector() {
        return ((net.kyori.adventure.text.EntityNBTComponent) wrappedObject).selector();
    }

    @Override
    public @NotNull EntityNBTComponent withSelector(@NotNull String selector) {
        return (EntityNBTComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.EntityNBTComponent) wrappedObject).selector(selector));
    }

    @Override
    public @NotNull EntityNBTComponent.Builder toBuilder() {
        return new AdventureEntityNBTBuilder(((net.kyori.adventure.text.EntityNBTComponent) wrappedObject).toBuilder());
    }

    @Override
    public @NotNull EntityNBTComponent withNbtPath(@NotNull String nbtPath) {
        return (EntityNBTComponent) super.withNbtPath(nbtPath);
    }

    @Override
    public @NotNull EntityNBTComponent withInterpret(boolean interpret) {
        return (EntityNBTComponent) super.withInterpret(interpret);
    }

    @Override
    public @NotNull EntityNBTComponent withSeparator(@Nullable Component separator) {
        return (EntityNBTComponent) super.withSeparator(separator);
    }

    public static class AdventureEntityNBTBuilder extends AdventureNBTComponent.AdventureNBTBuilder<
            net.kyori.adventure.text.EntityNBTComponent,
            EntityNBTComponent.Builder,
            EntityNBTComponent,
            net.kyori.adventure.text.EntityNBTComponent.Builder
            > implements EntityNBTComponent.Builder {

        public AdventureEntityNBTBuilder(net.kyori.adventure.text.EntityNBTComponent.Builder builder) {
            super(builder);
        }

        @Override
        public @NotNull EntityNBTComponent.Builder selector(@NotNull String selector) {
            getBuilder().selector(selector);
            return self();
        }
    }

}
