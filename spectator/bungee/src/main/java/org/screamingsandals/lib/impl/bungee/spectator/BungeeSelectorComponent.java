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

import net.md_5.bungee.api.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.impl.bungee.spectator.backports.SelectorPortedComponent;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SelectorComponent;

public class BungeeSelectorComponent extends BungeeComponent implements SelectorComponent {
    protected BungeeSelectorComponent(net.md_5.bungee.api.chat.@NotNull SelectorComponent wrappedObject) {
        super(wrappedObject);
    }

    @Override
    public @NotNull String pattern() {
        return ((net.md_5.bungee.api.chat.SelectorComponent) wrappedObject).getSelector();
    }

    @Override
    public @NotNull SelectorComponent withPattern(@NotNull String pattern) {
        var duplicate = (net.md_5.bungee.api.chat.SelectorComponent) wrappedObject.duplicate();
        duplicate.setSelector(pattern);
        return (SelectorComponent) AbstractBungeeBackend.wrapComponent(duplicate);
    }

    @Override
    public SelectorComponent.@NotNull Builder toBuilder() {
        var duplicate = (net.md_5.bungee.api.chat.SelectorComponent) wrappedObject.duplicate();
        if (AbstractBungeeBackend.COMPONENTS_PORTED_SUCCESSFULLY) {
            return new MultipleImplementationsBuilder(duplicate);
        }
        return new BungeeSelectorBuilder(duplicate);
    }

    @Override
    public @Nullable Component separator() {
        return null; // WHERE IS IT ???
    }

    @Override
    public @NotNull SelectorComponent withSeparator(@Nullable Component separator) {
        return this; // WHERE IS IT ???
    }

    public static class BungeeSelectorBuilder extends BungeeBuilder<SelectorComponent, SelectorComponent.Builder, net.md_5.bungee.api.chat.SelectorComponent> implements SelectorComponent.Builder {

        public BungeeSelectorBuilder(net.md_5.bungee.api.chat.@NotNull SelectorComponent component) {
            super(component);
        }

        @Override
        public SelectorComponent.@NotNull Builder pattern(@NotNull String pattern) {
            component.setSelector(pattern);
            return this;
        }

        @Override
        public SelectorComponent.@NotNull Builder separator(@Nullable Component separator) {
            // Hey md_5, I hate you with all my hearth
            return this;
        }
    }

    public static class MultipleImplementationsBuilder extends BungeeBuilder<SelectorComponent, SelectorComponent.Builder, BaseComponent> implements SelectorComponent.Builder {
        public MultipleImplementationsBuilder(net.md_5.bungee.api.chat.@NotNull SelectorComponent component) {
            super(component);
        }

        public MultipleImplementationsBuilder(@NotNull SelectorPortedComponent component) {
            super(component);
        }

        @Override
        public SelectorComponent.@NotNull Builder pattern(@NotNull String pattern) {
            if (component instanceof net.md_5.bungee.api.chat.SelectorComponent) {
                ((net.md_5.bungee.api.chat.SelectorComponent) component).setSelector(pattern);
            } else if (component instanceof SelectorPortedComponent) {
                ((SelectorPortedComponent) component).setSelector(pattern);
            }
            return this;
        }

        @Override
        public SelectorComponent.@NotNull Builder separator(@Nullable Component separator) {
            if (component instanceof net.md_5.bungee.api.chat.SelectorComponent && separator != null) {
                // Separator is not supported yet by md_5's chat api, replace with ported selector
                var old = (net.md_5.bungee.api.chat.SelectorComponent) component;
                var newComponent = new SelectorPortedComponent(old.getSelector());
                newComponent.copyFrom(old);
                component = newComponent;
            }

            if (component instanceof SelectorPortedComponent) {
                ((SelectorPortedComponent) component).setSeparator(separator == null ? null : separator.as(BaseComponent.class));
            }
            return this;
        }
    }
}
