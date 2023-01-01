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

import net.kyori.adventure.text.NBTComponentBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.NBTComponent;

public abstract class AdventureNBTComponent<C extends net.kyori.adventure.text.NBTComponent<C,?>> extends AdventureComponent implements NBTComponent {
    public AdventureNBTComponent(@NotNull C wrappedObject) {
        super(wrappedObject);
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public String nbtPath() {
        return ((C) wrappedObject).nbtPath();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean interpret() {
        return ((C) wrappedObject).interpret();
    }

    @Override
    @Nullable
    public Component separator() {
        try {
            return AdventureBackend.wrapComponent(((net.kyori.adventure.text.NBTComponent<?,?>) wrappedObject).separator());
        } catch (Throwable ignored) {
            return null; // added in Adventure 4.8.0
        }
    }

    @Override
    @NotNull
    public NBTComponent withSeparator(@Nullable Component separator) {
        try {
            return (NBTComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.NBTComponent<?,?>) wrappedObject).separator(separator == null ? null : separator.as(net.kyori.adventure.text.Component.class)));
        } catch (Throwable ignored) {
            // added in Adventure 4.8.0
        }
        return this;
    }

    @Override
    @NotNull
    public NBTComponent withNbtPath(@NotNull String nbtPath) {
        return (NBTComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.NBTComponent<?,?>) wrappedObject).nbtPath(nbtPath));
    }

    @Override
    @NotNull
    public NBTComponent withInterpret(boolean interpret) {
        return (NBTComponent) AdventureBackend.wrapComponent(((net.kyori.adventure.text.NBTComponent<?,?>) wrappedObject).interpret(interpret));
    }

    public static class AdventureNBTBuilder<
            A extends net.kyori.adventure.text.NBTComponent<A, D>,
            B extends NBTComponent.Builder<B, C>,
            C extends NBTComponent,
            D extends NBTComponentBuilder<A, D>
            > extends AdventureBuilder<A, B, C, D> implements NBTComponent.Builder<B, C> {

        public AdventureNBTBuilder(D builder) {
            super(builder);
        }

        @Override
        @NotNull
        public B nbtPath(@NotNull String nbtPath) {
            getBuilder().nbtPath(nbtPath);
            return self();
        }

        @Override
        @NotNull
        public B interpret(boolean interpret) {
            getBuilder().interpret(interpret);
            return self();
        }

        @Override
        @NotNull
        public B separator(@Nullable Component separator) {
            try {
                getBuilder().separator(separator == null ? null : separator.as(net.kyori.adventure.text.Component.class));
            } catch (Throwable ignored) {
                // added in Adventure 4.8.0
            }
            return self();
        }
    }
}
