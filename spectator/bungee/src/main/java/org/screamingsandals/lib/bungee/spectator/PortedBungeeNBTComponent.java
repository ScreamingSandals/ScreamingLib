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

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.bungee.spectator.backports.NBTPortedComponent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.NBTComponent;

public abstract class PortedBungeeNBTComponent extends BungeeComponent implements NBTComponent {
    protected PortedBungeeNBTComponent(@NotNull NBTPortedComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String nbtPath() {
        return ((NBTPortedComponent) wrappedObject).getNbtPath();
    }

    @Override
    public @NotNull NBTComponent withNbtPath(@NotNull String nbtPath) {
        var duplicate = ((NBTPortedComponent) wrappedObject).duplicate();
        duplicate.setNbtPath(nbtPath);
        return (NBTComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public boolean interpret() {
        return ((NBTPortedComponent) wrappedObject).isInterpret();
    }

    @Override
    public @NotNull NBTComponent withInterpret(boolean interpret) {
        var duplicate = ((NBTPortedComponent) wrappedObject).duplicate();
        duplicate.setInterpret(interpret);
        return (NBTComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public @Nullable Component separator() {
        var component = ((NBTPortedComponent) wrappedObject).getSeparator();
        return component == null ? null : AbstractBungeeBackend.wrapComponent(component);
    }

    @Override
    public @NotNull NBTComponent withSeparator(@Nullable Component separator) {
        var duplicate = ((NBTPortedComponent) wrappedObject).duplicate();
        duplicate.setSeparator(separator == null ? null : separator.as(BaseComponent.class));
        return (NBTComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    public abstract static class BungeeNBTBuilder<C extends NBTComponent, B extends NBTComponent.Builder<B, C>, O extends NBTPortedComponent> extends BungeeBuilder<C, B, O> implements NBTComponent.Builder<B, C> {
        public BungeeNBTBuilder(@NotNull O component) {
            super(component);
        }

        @Override
        public @NotNull B nbtPath(@NotNull String nbtPath) {
            component.setNbtPath(nbtPath);
            return self();
        }

        @Override
        public @NotNull B interpret(boolean interpret) {
            component.setInterpret(interpret);
            return self();
        }

        @Override
        public @NotNull B separator(@Nullable Component separator) {
            component.setSeparator(separator == null ? null : separator.as(BaseComponent.class));
            return self();
        }
    }
}
