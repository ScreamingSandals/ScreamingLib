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
import org.screamingsandals.lib.bungee.spectator.backports.SelectorPortedComponent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SelectorComponent;

public class PortedBungeeSelectorComponent extends BungeeComponent implements SelectorComponent {
    protected PortedBungeeSelectorComponent(@NotNull SelectorPortedComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String pattern() {
        return ((SelectorPortedComponent) wrappedObject).getSelector();
    }

    @Override
    public @NotNull SelectorComponent withPattern(@NotNull String pattern) {
        var duplicate = (SelectorPortedComponent) wrappedObject.duplicate();
        duplicate.setSelector(pattern);
        return (SelectorComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public SelectorComponent.@NotNull Builder toBuilder() {
        var duplicate = (SelectorPortedComponent) wrappedObject.duplicate();
        return new BungeeSelectorBuilder(duplicate);
    }

    @Override
    public @Nullable Component separator() {
        var component = ((SelectorPortedComponent) wrappedObject).getSeparator();
        return component != null ? AbstractBungeeBackend.wrapComponent(component) : null;
    }

    @Override
    public @NotNull SelectorComponent withSeparator(@Nullable Component separator) {
        var duplicate = (SelectorPortedComponent) wrappedObject.duplicate();
        duplicate.setSeparator(separator == null ? null : separator.as(BaseComponent.class));
        return (SelectorComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    public static class BungeeSelectorBuilder extends BungeeBuilder<SelectorComponent, SelectorComponent.Builder, SelectorPortedComponent> implements SelectorComponent.Builder {

        public BungeeSelectorBuilder(@NotNull SelectorPortedComponent component) {
            super(component);
        }

        @Override
        public SelectorComponent.@NotNull Builder pattern(@NotNull String pattern) {
            component.setSelector(pattern);
            return this;
        }

        @Override
        public SelectorComponent.@NotNull Builder separator(@Nullable Component separator) {
            component.setSeparator(separator == null ? null : separator.as(BaseComponent.class));
            return this;
        }
    }
}
