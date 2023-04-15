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

package org.screamingsandals.lib.impl.bungee.spectator;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bungee.spectator.backports.EntityNBTPortedComponent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.EntityNBTComponent;

public class PortedBungeeEntityNBTComponent extends PortedBungeeNBTComponent implements EntityNBTComponent {
    protected PortedBungeeEntityNBTComponent(@NotNull EntityNBTPortedComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String selector() {
        return ((EntityNBTPortedComponent) wrappedObject).getSelector();
    }

    @Override
    public @NotNull EntityNBTComponent withSelector(@NotNull String selector) {
        var duplicate = ((EntityNBTPortedComponent) wrappedObject).duplicate();
        duplicate.setSelector(selector);
        return (EntityNBTComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public EntityNBTComponent.@NotNull Builder toBuilder() {
        var duplicate = ((EntityNBTPortedComponent) wrappedObject).duplicate();
        return new BungeeEntityNBTBuilder(duplicate);
    }

    @Override
    public @NotNull EntityNBTComponent withInterpret(boolean interpret) {
        return (EntityNBTComponent) super.withInterpret(interpret);
    }

    @Override
    public @NotNull EntityNBTComponent withNbtPath(@NotNull String nbtPath) {
        return (EntityNBTComponent) super.withNbtPath(nbtPath);
    }

    @Override
    public @NotNull EntityNBTComponent withSeparator(@Nullable Component separator) {
        return (EntityNBTComponent) super.withSeparator(separator);
    }

    public static class BungeeEntityNBTBuilder extends BungeeNBTBuilder<EntityNBTComponent, EntityNBTComponent.Builder, EntityNBTPortedComponent> implements EntityNBTComponent.Builder {
        public BungeeEntityNBTBuilder(@NotNull EntityNBTPortedComponent component) {
            super(component);
        }

        @Override
        public EntityNBTComponent.@NotNull Builder selector(@NotNull String selector) {
            component.setSelector(selector);
            return this;
        }
    }
}
